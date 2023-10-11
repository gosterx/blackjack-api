package wallet.domain

import doobie.util.meta.Meta
import doobie.postgres.implicits.*

enum TransactionType:
  case DEBIT, CREDIT

object TransactionType:
  given Meta[TransactionType] =
    pgEnumString[TransactionType]("TRANSACTION_TYPE", TransactionType.valueOf, _.toString)
