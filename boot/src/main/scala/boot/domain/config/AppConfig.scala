package boot.domain.config

import boot.domain.config.AppConfig.{ DatabaseConfig, HttpConfig, JwtConfig }
import com.comcast.ip4s.*
import ciris.*
import cats.effect.kernel.Async
import cats.syntax.all.*

final case class AppConfig(
    http: HttpConfig,
    jwt: JwtConfig,
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
      ).parMapN(HttpConfig.apply).load[F]

  final case class JwtConfig(
      expiration: Boolean,
      secret: String
  )

  object JwtConfig:
    def load[F[_]: Async]: F[JwtConfig] =
      (
        default("false").as[Boolean],
        default("secretKey").as[String]
      ).parMapN(JwtConfig.apply).load[F]

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
      ).parMapN(DatabaseConfig.apply).load[F]

  def load[F[_]: Async]: F[AppConfig] =
    for
      httpConfig     <- HttpConfig.load
      jwtConfig      <- JwtConfig.load
      databaseConfig <- DatabaseConfig.load
    yield AppConfig(httpConfig, jwtConfig, databaseConfig)
