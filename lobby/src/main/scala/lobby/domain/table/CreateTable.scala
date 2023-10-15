package lobby.domain.table

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class CreateTable(
    id: String,
    configuration: TableConfiguration
)

object CreateTable:
  given Decoder[CreateTable] = deriveDecoder[CreateTable]
