package blackjack.domain.user

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

final case class User(
    id: Int,
    loginName: String,
    firstName: String,
    lastName: String,
    email: String
)

object User:
  given Encoder[User] = deriveEncoder
