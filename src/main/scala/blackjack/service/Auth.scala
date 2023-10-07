package blackjack.service

import blackjack.domain.user.{ CreateUser, UserInfo }
import blackjack.repository.UserRepository
import cats.effect.kernel.Sync
import cats.syntax.all.*
import tsec.passwordhashers.jca.BCrypt
import doobie.util.transactor.Transactor
import doobie.implicits.*

trait Auth[F[_]]:
  def signUp(request: CreateUser): F[Unit]

object Auth:
  def of[F[_]: Sync]()(using transactor: Transactor[F]): Auth[F] = new Auth[F]:
    override def signUp(request: CreateUser): F[Unit] =
      for
        passwordHash <- BCrypt.hashpw[F](request.password)
        userInfo = UserInfo.fromCreateUser(request, passwordHash)
        _ <- UserRepository.insert(userInfo).transact(transactor)
      yield ()
