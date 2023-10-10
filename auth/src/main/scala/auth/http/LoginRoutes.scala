package auth.http

import auth.domain.user.{ CreateUser, LoginUser }
import auth.service.Auth
import cats.syntax.all.*
import cats.effect.Concurrent
import org.http4s.{ HttpRoutes, ResponseCookie }
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityDecoder.*

class LoginRoutes[F[_]: Concurrent](auth: Auth[F]) extends Http4sDsl[F]:
  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "signUp" =>
      for
        createUser <- req.as[CreateUser]
        token      <- auth.signUp(createUser)
        response   <- Ok("Signed Up!").map(_.addCookie(ResponseCookie("token", token)))
      yield response

    case req @ POST -> Root / "login" =>
      for
        loginUser <- req.as[LoginUser]
        token     <- auth.login(loginUser)
        response  <- Ok("Logged In!").map(_.addCookie(ResponseCookie("token", token)))
      yield response
  }

  final val routes = httpRoutes

object LoginRoutes:
  def apply[F[_]: Concurrent](auth: Auth[F]): LoginRoutes[F] = new LoginRoutes[F](auth)
