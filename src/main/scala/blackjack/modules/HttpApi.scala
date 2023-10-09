package blackjack.modules

import blackjack.http.{ AuthRoutes, JwtProcessor, LoginRoutes }
import blackjack.domain.user.*
import cats.data.{ Kleisli, OptionT }
import cats.effect.kernel
import cats.effect.kernel.{ Concurrent, Sync }
import cats.syntax.all.*
import org.http4s.*
import org.http4s.server.*
import org.http4s.syntax.all.*

final case class HttpApi[F[_]: Concurrent](
    jwtGenerator: JwtProcessor[F],
    services: ServiceModule[F]
):
  private val authMiddleware = AuthMiddleware[F, User] {
    Kleisli.apply { request =>
      OptionT.fromOption {
        for
          token <- request.cookies.find(_.name == "token").map(_.content)
          user  <- jwtGenerator.decode(token)
        yield user
      }
    }
  }

  private val loginRoutes  = LoginRoutes[F](services.auth)
  private val authedRoutes = AuthRoutes[F]()

  val httpApp: HttpApp[F] = (loginRoutes.routes <+> authMiddleware(authedRoutes.routes)).orNotFound
