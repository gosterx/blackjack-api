package blackjack.modules

import blackjack.domain.config.AppConfig
import blackjack.http.JwtGenerator
import blackjack.service.Auth
import cats.effect.kernel.{ Resource, Sync }
import cats.effect.syntax.all.*
import doobie.util.transactor.Transactor
import org.typelevel.log4cats.Logger

final case class ServiceModule[F[_]](
    auth: Auth[F]
)

object ServiceModule:
  def make[F[_]: Sync](
      repositoryModule: RepositoryModule[F],
      config: AppConfig
  ): Resource[F, ServiceModule[F]] =
    given Transactor[F] = repositoryModule.postgres
    for
      auth <- Auth.of[F](JwtGenerator[F](config.jwt.secret)).toResource
    yield ServiceModule(auth)
