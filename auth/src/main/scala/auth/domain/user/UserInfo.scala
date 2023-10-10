package auth.domain.user

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.BCrypt

final case class UserInfo(
    loginName: String,
    firstName: String,
    lastName: String,
    email: String,
    passwordHash: String
)

object UserInfo:
  def fromCreateUser(createUser: CreateUser, passwordHash: String): UserInfo =
    UserInfo(
      loginName = createUser.loginName,
      firstName = createUser.firstName,
      lastName = createUser.lastName,
      email = createUser.email,
      passwordHash = passwordHash
    )
