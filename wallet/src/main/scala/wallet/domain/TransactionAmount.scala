package wallet.domain

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class TransactionAmount(amount: BigDecimal)

object TransactionAmount:
  given Decoder[TransactionAmount] = deriveDecoder[TransactionAmount]
