package blackjack.modules

import blackjack.domain.config.AppConfig.DatabaseConfig
import cats.syntax.all.*
import cats.effect.syntax.all.*
import cats.effect.kernel.{ Async, Resource, Sync }
import com.zaxxer.hikari.HikariConfig
import doobie.Transactor
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

final case class RepositoryModule[F[_]](
    postgres: Transactor[F]
)

object RepositoryModule:
  private def hikari[F[_]: Sync](databaseConfig: DatabaseConfig): F[HikariConfig] =
    Sync[F].pure(new HikariConfig()).flatMap { config =>
      Sync[F].delay {
        config.setDriverClassName(databaseConfig.driver)
        config.setJdbcUrl(databaseConfig.url)
        config.setUsername(databaseConfig.user)
        config.setPassword(databaseConfig.password)
        config.setMaximumPoolSize(databaseConfig.connectionPoolSize)
        config
      }
    }

  def make[F[_]: Async](config: DatabaseConfig): Resource[F, RepositoryModule[F]] =
    for
      hikari     <- hikari(config).toResource
      _          <- FlyWayMigrations.migrate(config).toResource
      transactor <- HikariTransactor.fromHikariConfig[F](hikari)
    yield RepositoryModule(transactor)
