package wallet.domain

import java.time.Instant

final case class Transaction(
    id: Int,
    userAccountId: Int,
    transactionType: TransactionType,
    amount: BigDecimal,
    createdAt: Instant
)
