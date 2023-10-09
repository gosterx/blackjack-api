package blackjack.http

import blackjack.domain.user.{ CreateUser, LoginUser }
import blackjack.service.Auth
import cats.syntax.all.*
import cats.effect.Concurrent
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityDecoder.*

class LoginRoutes[F[_]: Concurrent](auth: Auth[F]) extends Http4sDsl[F]:
  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "signUp" =>
      for
        createUser <- req.as[CreateUser]
        _          <- auth.signUp(createUser)
        response   <- Ok("")
      yield response

    case req @ POST -> Root / "login" =>
      for
        loginUser <- req.as[LoginUser]
        _         <- auth.login(loginUser)
        response  <- Ok("")
      yield response
  }

  final val routes = httpRoutes

object LoginRoutes:
  def apply[F[_]: Concurrent](auth: Auth[F]): LoginRoutes[F] = new LoginRoutes[F](auth)
