package blackjack.domain.user

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class LoginUser(
    loginName: String,
    password: String
)

object LoginUser:
  given Decoder[LoginUser] = deriveDecoder[LoginUser]
