package boot.modules

import auth.http.{ AuthRoutes, JwtProcessor, LoginRoutes }
import auth.domain.user.*
import wallet.http.WalletRoutes
import cats.data.{ Kleisli, OptionT }
import cats.effect.kernel
import cats.effect.kernel.Async
import cats.syntax.all.*
import org.http4s.*
import org.http4s.server.*
import org.http4s.server.middleware.{ ErrorHandling, Logger }
import org.http4s.syntax.all.*
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory
import cats.evidence.As

final case class HttpApi[F[_]: Async: LoggerFactory](
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

  private val walletRoutes = WalletRoutes[F](services.wallet)

  private val routes = Router(
    "api/wallet" -> authMiddleware(walletRoutes.routes),
    "api/auth"   -> loginRoutes.routes
  )

  val httpApp: HttpApp[F] =
    ErrorHandling.httpRoutes[F](
      Logger.httpRoutes[F](
        logHeaders = true,
        logBody = true
      )(routes)
    ).orNotFound
