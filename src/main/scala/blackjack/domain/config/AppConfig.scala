package blackjack.domain.config

import blackjack.domain.config.AppConfig.{ DatabaseConfig, HttpConfig }
import com.comcast.ip4s.*
import ciris.*
import cats.effect.kernel.Async
import cats.syntax.all.*

final case class AppConfig(
    http: HttpConfig,
    db: DatabaseConfig
)

object AppConfig:
  final case class HttpConfig(
      host: String,
      port: Port
  )

  object HttpConfig:
    def load[F[_]: Async]: F[HttpConfig] =
      (
        default("0.0.0.0").as[String],
        default(port"8080").as[Port]
      ).parMapN { (host, port) => HttpConfig(host, port) }.load[F]

  final case class DatabaseConfig(
      url: String,
      user: String,
      password: String,
      driver: String,
      connectionPoolSize: Int
  )

  object DatabaseConfig:
    def load[F[_]: Async]: F[DatabaseConfig] =
      (
        default("jdbc:postgresql://localhost:5432/postgres").as[String],
        default("postgres").as[String],
        default("postgres").as[String],
        default("org.postgresql.Driver").as[String],
        default(10).as[Int]
      ).parMapN { (url, user, password, driver, connectionPoolSize) =>
        DatabaseConfig(url, user, password, driver, connectionPoolSize)
      }.load[F]

  def load[F[_]: Async]: F[AppConfig] =
    for
      httpConfig     <- HttpConfig.load
      databaseConfig <- DatabaseConfig.load
    yield AppConfig(httpConfig, databaseConfig)
