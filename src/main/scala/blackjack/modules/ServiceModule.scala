package blackjack.modules

import blackjack.service.Auth
import cats.effect.kernel.{ Resource, Sync }
import cats.effect.syntax.all.*
import doobie.util.transactor.Transactor

final case class ServiceModule[F[_]](
    auth: Auth[F]
)

object ServiceModule:
  def make[F[_]: Sync](repositoryModule: RepositoryModule[F]): Resource[F, ServiceModule[F]] =
    given Transactor[F] = repositoryModule.postgres
    Resource.pure(ServiceModule(Auth.of[F]))
