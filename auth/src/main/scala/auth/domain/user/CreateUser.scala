package auth.domain.user

import io.circe.{ Codec, Decoder }
import io.circe.generic.semiauto.{ deriveCodec, deriveDecoder }

final case class CreateUser(
    loginName: String,
    firstName: String,
    lastName: String,
    email: String,
    password: String
)

object CreateUser:
  given Decoder[CreateUser] = deriveDecoder[CreateUser]
