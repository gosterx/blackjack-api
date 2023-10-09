package blackjack.http

import blackjack.service.Auth
import blackjack.domain.user.User
import cats.syntax.all.*
import cats.effect.Concurrent
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl

class AuthRoutes[F[_]: Concurrent]() extends Http4sDsl[F]:
  val routes = AuthedRoutes.of[User, F] {
    case GET -> Root / "hello" as user =>
      Ok(user.toString)
  }

object AuthRoutes:
  def apply[F[_]: Concurrent](): AuthRoutes[F] = new AuthRoutes[F]()
