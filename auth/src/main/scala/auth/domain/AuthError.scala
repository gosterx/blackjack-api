package auth.domain

import io.circe.Encoder

sealed trait AuthError extends Throwable:
  def message: String
  override def getMessage(): String = message

object AuthError:
  given encoder: Encoder[Throwable] = Encoder.forProduct1("message")(_.getMessage())

  case object DuplicateLoginName extends AuthError:
    override def message: String = "Duplicate login name"

  case object IncorrectLoginName extends AuthError:
    override def message: String = "Incorrect login name"

  case object IncorrectPassword extends AuthError:
    override def message: String = "Incorrect password"
