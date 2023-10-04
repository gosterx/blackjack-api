package blackjack

import blackjack.http.Ember
import cats.effect.{ IO, IOApp }
import com.comcast.ip4s.Port
import fs2.Stream

object Main extends IOApp.Simple:
  override def run: IO[Unit] =
    Ember.default[IO](Port.fromInt(8080).get).useForever
