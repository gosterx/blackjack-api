package blackjack

import blackjack.domain.config.AppConfig
import blackjack.http.{ Ember, JwtProcessor }
import blackjack.modules.{ HttpApi, RepositoryModule, ServiceModule }
import cats.Show
import cats.effect.{ IO, IOApp }
import com.comcast.ip4s.Port
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple:
  override def run: IO[Unit] =
    (for
      config <- AppConfig.load[IO].toResource
      jwtProcessor = JwtProcessor[IO](config.jwt.secret)
      repository <- RepositoryModule.make[IO](config.db)
      service    <- ServiceModule.make[IO](jwtProcessor, repository, config)
      httpApp = HttpApi[IO](jwtProcessor, service).httpApp
      server <- Ember.default[IO](httpApp, config.http.port)
    yield server).useForever
