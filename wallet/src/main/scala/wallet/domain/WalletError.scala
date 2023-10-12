package wallet.domain

import io.circe.Encoder

sealed trait WalletError extends Throwable:
  def message: String
  override def getMessage(): String = message

object WalletError:
  given encoder: Encoder[Throwable] = Encoder.forProduct1("message")(_.getMessage())

  case object InsufficientFunds extends WalletError:
    override def message: String = "Insufficient funds"
