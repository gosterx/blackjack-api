package blackjack.modules

import blackjack.domain.config.AppConfig.DatabaseConfig
import cats.effect.kernel.Sync
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.flywaydb.core.Flyway

object FlyWayMigrations:
  def migrate[F[_]: Sync](config: DatabaseConfig): F[Int] =
    Sync[F].delay {
      unsafeMigrate(config)
    }

  private def unsafeMigrate(config: DatabaseConfig): Int =
    val m: FluentConfiguration = Flyway.configure.dataSource(
      config.url,
      config.user,
      config.password
    )
      .group(true)
      .outOfOrder(false)
      .locations("classpath:db/migration")

    m.load().migrate().migrationsExecuted
