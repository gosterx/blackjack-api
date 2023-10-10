package auth.domain.user

import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }

final case class User(
    id: Int,
    loginName: String,
    firstName: String,
    lastName: String,
    email: String
)

object User:
  given Encoder[User] = deriveEncoder
  given Decoder[User] = deriveDecoder
