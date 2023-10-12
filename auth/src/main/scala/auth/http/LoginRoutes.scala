package auth.http

import auth.domain.user.{ CreateUser, LoginUser }
import auth.domain.AuthError.*
import auth.domain.AuthError.encoder
import auth.service.Auth
import cats.syntax.all.*
import cats.effect.Concurrent
import org.http4s.{ HttpRoutes, ResponseCookie }
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityDecoder.*
import org.http4s.circe.CirceEntityEncoder.*
import cats.data.Kleisli
import cats.data.OptionT
import io.circe.syntax.EncoderOps

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

  final val routes = httpRoutes.recoverWith {
    case err @ DuplicateLoginName => Kleisli { _ =>
        OptionT.liftF(Conflict(err.asJson))
      }
    case err @ IncorrectLoginName => Kleisli { _ =>
        OptionT.liftF(BadRequest(err.asJson))
      }
    case err @ IncorrectPassword => Kleisli { _ =>
        OptionT.liftF(BadRequest(err.asJson))
      }
  }

object LoginRoutes:
  def apply[F[_]: Concurrent](auth: Auth[F]): LoginRoutes[F] = new LoginRoutes[F](auth)
