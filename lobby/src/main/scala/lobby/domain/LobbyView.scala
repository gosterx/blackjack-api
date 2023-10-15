package lobby.domain

import lobby.domain.table.Table
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

final case class LobbyView(tables: List[Table])

object LobbyView:
  given Encoder[LobbyView] = deriveEncoder[LobbyView]
