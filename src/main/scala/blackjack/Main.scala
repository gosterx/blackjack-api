package blackjack

import blackjack.domain.config.AppConfig
import blackjack.http.Ember
import blackjack.modules.{ HttpApi, RepositoryModule, ServiceModule }
import cats.Show
import cats.effect.{ IO, IOApp }
import com.comcast.ip4s.Port

object Main extends IOApp.Simple:
  override def run: IO[Unit] =
    (for
      config     <- AppConfig.load[IO].toResource
      repository <- RepositoryModule.make[IO](config.db)
      service    <- ServiceModule.make[IO](repository)
      httpApp = HttpApi[IO](service).httpApp
      server <- Ember.default[IO](httpApp, config.http.port)
    yield server).useForever
