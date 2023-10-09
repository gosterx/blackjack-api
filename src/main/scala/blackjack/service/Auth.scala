package blackjack.service

import blackjack.domain.user.{ CreateUser, LoginUser, UserInfo }
import blackjack.repository.UserRepository
import cats.effect.kernel.Sync
import cats.syntax.all.*
import tsec.passwordhashers.jca.BCrypt
import tsec.passwordhashers.PasswordHash
import doobie.util.transactor.Transactor
import doobie.implicits.*
import tsec.common.{ VerificationFailed, Verified }

trait Auth[F[_]]:
  def signUp(request: CreateUser): F[Unit]
  def login(request: LoginUser): F[Unit]

object Auth:
  def of[F[_]: Sync](using transactor: Transactor[F]): Auth[F] = new Auth[F]:
    override def signUp(request: CreateUser): F[Unit] =
      for
        passwordHash <- BCrypt.hashpw[F](request.password)
        userInfo = UserInfo.fromCreateUser(request, passwordHash)
        _ <- UserRepository.insert(userInfo).transact(transactor)
      yield ()

    override def login(request: LoginUser): F[Unit] =
      for
        existingPasswordHash <- UserRepository.selectPasswordHash(request.loginName).transact(transactor)
        validationResult     <- BCrypt.checkpwBool[F](request.password, PasswordHash(existingPasswordHash))
        _                    <- Sync[F].raiseWhen(!validationResult)(new RuntimeException("Hash validation failed"))
      yield ()
