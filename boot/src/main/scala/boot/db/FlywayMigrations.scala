package boot.db

import boot.domain.config.AppConfig.DatabaseConfig
import cats.effect.kernel.Sync
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration

object FlywayMigrations:
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
