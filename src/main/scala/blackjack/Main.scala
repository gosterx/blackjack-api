package blackjack

import blackjack.domain.config.AppConfig
import blackjack.http.Ember
import blackjack.modules.RepositoryModule
import cats.Show
import cats.effect.{ IO, IOApp }
import com.comcast.ip4s.Port

object Main extends IOApp.Simple:
  override def run: IO[Unit] =
    (for
      config     <- AppConfig.load[IO].toResource
      repository <- RepositoryModule.make[IO](config.db)
      server     <- Ember.default[IO](Port.fromInt(8080).get)
    yield server).useForever
