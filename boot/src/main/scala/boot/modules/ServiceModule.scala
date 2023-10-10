package boot.modules

import boot.domain.config.AppConfig
import auth.http.JwtProcessor
import auth.service.Auth
import cats.effect.kernel.{ Resource, Sync }
import cats.effect.syntax.all.*
import doobie.util.transactor.Transactor
import org.typelevel.log4cats.Logger

final case class ServiceModule[F[_]](
    auth: Auth[F]
)

object ServiceModule:
  def make[F[_]: Sync](
      jwtProcessor: JwtProcessor[F],
      repositoryModule: RepositoryModule[F],
      config: AppConfig
  ): Resource[F, ServiceModule[F]] =
    given Transactor[F] = repositoryModule.postgres
    for
      auth <- Auth.of[F](jwtProcessor).toResource
    yield ServiceModule(auth)
