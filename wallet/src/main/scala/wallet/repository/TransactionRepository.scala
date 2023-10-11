package wallet.repository

import wallet.domain.TransactionType
import cats.syntax.all.*
import doobie.{ ConnectionIO, Fragment }
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.meta.Meta

trait TransactionRepository[F[_]]:
  def debit(accountId: Int, amount: BigDecimal): F[Unit]
  def credit(accountId: Int, amount: BigDecimal): F[Unit]

object TransactionRepository extends TransactionRepository[ConnectionIO]:
  private object SQL:
    def insert(accountId: Int, amount: BigDecimal, txType: TransactionType): Fragment =
      fr"""
          |INSERT INTO transactions (user_account_id, transaction_type, amount)
          |VALUES ($accountId, $txType, $amount)
          """.stripMargin

  override def debit(accountId: Int, amount: BigDecimal): ConnectionIO[Unit] =
    SQL.insert(accountId, amount, TransactionType.DEBIT).update.run.void

  override def credit(accountId: Int, amount: BigDecimal): ConnectionIO[Unit] =
    SQL.insert(accountId, amount, TransactionType.CREDIT).update.run.void
