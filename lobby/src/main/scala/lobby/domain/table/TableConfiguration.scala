package lobby.domain.table

import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }

final case class TableConfiguration(
    maxParticipants: Int,
    minBet: BigDecimal,
    maxBet: BigDecimal
)

object TableConfiguration:
  given Encoder[TableConfiguration] = deriveEncoder[TableConfiguration]
  given Decoder[TableConfiguration] = deriveDecoder[TableConfiguration]
