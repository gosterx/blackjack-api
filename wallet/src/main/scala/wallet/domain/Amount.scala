package wallet.domain

import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }

final case class Amount(amount: BigDecimal)

object Amount:
  given Decoder[Amount] = deriveDecoder[Amount]
  given Encoder[Amount] = deriveEncoder[Amount]
