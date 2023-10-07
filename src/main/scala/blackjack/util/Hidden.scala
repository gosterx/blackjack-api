package blackjack.util

import io.circe.{ Decoder, Encoder }

final case class Hidden[T: Encoder: Decoder](value: T):
  override def toString: String = "***"
