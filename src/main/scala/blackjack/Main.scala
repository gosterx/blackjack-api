package blackjack

import blackjack.http.Ember
import cats.Show
import cats.effect.{ IO, IOApp }
import com.comcast.ip4s.Port

object Main extends IOApp.Simple:
  override def run: IO[Unit] =
    Ember.default[IO](Port.fromInt(8080).get).useForever
