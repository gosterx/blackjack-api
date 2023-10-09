package blackjack.service

import blackjack.domain.user.{ CreateUser, LoginUser, UserInfo }
import blackjack.http.JwtGenerator
import blackjack.repository.UserRepository
import cats.effect.kernel.Sync
import cats.syntax.all.*
import tsec.passwordhashers.jca.BCrypt
import tsec.passwordhashers.PasswordHash
import doobie.util.transactor.Transactor
import doobie.implicits.*
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait Auth[F[_]]:
  def signUp(request: CreateUser): F[String]
  def login(request: LoginUser): F[String]

object Auth:
  def of[F[_]: Sync](jwtGenerator: JwtGenerator[F])(using transactor: Transactor[F]): F[Auth[F]] =
    for
      logger <- Slf4jLogger.create[F]
    yield new Auth[F]:
      override def signUp(request: CreateUser): F[String] =
        for
          passwordHash <- BCrypt.hashpw[F](request.password)
          userInfo = UserInfo.fromCreateUser(request, passwordHash.trim)
          user  <- UserRepository.insert(userInfo).transact(transactor)
          token <- jwtGenerator.generateToken(user)
          _     <- logger.info("User was successfully signed up")
        yield token

      override def login(request: LoginUser): F[String] =
        for
          existingPasswordHash <- UserRepository.selectPasswordHash(request.loginName).transact(transactor)
          validationResult     <- BCrypt.checkpwBool[F](request.password, PasswordHash(existingPasswordHash))
          _                    <- Sync[F].raiseWhen(!validationResult)(new RuntimeException("Hash validation failed"))
          user                 <- UserRepository.select(request.loginName).transact(transactor)
          token                <- jwtGenerator.generateToken(user)
          _                    <- logger.info("User was successfully logged in")
        yield token
