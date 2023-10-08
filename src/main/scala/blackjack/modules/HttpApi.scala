package blackjack.modules

import blackjack.http.LoginRoutes
import cats.effect.kernel.Concurrent
import org.http4s.HttpApp
import org.http4s.syntax.all.*

final case class HttpApi[F[_]: Concurrent](
    services: ServiceModule[F]
):
  private val loginRoutes = LoginRoutes[F](services.auth)

  val httpApp: HttpApp[F] = loginRoutes.routes.orNotFound
