package blackjack

import blackjack.domain.config.AppConfig
import blackjack.http.Ember
import blackjack.modules.{ HttpApi, RepositoryModule, ServiceModule }
import cats.Show
import cats.effect.{ IO, IOApp }
import com.comcast.ip4s.Port
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple:
  given Logger[IO] = Slf4jLogger.getLogger[IO]

  override def run: IO[Unit] =
    (for
      config     <- AppConfig.load[IO].toResource
      repository <- RepositoryModule.make[IO](config.db)
      service    <- ServiceModule.make[IO](repository, config)
      httpApp = HttpApi[IO](service).httpApp
      server <- Ember.default[IO](httpApp, config.http.port)
    yield server).useForever
