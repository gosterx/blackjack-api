package wallet.service

import wallet.repository.TransactionRepository
import org.typelevel.log4cats.slf4j.Slf4jLogger
import cats.effect.kernel.Sync
import cats.syntax.all.*
import doobie.util.transactor.Transactor
import doobie.implicits.*

trait Wallet[F[_]]:
  def debit(accountId: Int, amount: BigDecimal): F[Unit]
  def credit(accountId: Int, amount: BigDecimal): F[Unit]
  def balance(accountId: Int): F[Unit]

object Wallet:
  def of[F[_]: Sync](using transactor: Transactor[F]): F[Wallet[F]] =
    for
      logger <- Slf4jLogger.create[F]
    yield new Wallet[F]:

      override def debit(accountId: Int, amount: BigDecimal): F[Unit] =
        for
          _ <- TransactionRepository.debit(accountId, amount).transact(transactor)
          _ <- logger.info(s"Withdrawal $amount from the account with $accountId identifier number")
        yield ()

      override def credit(accountId: Int, amount: BigDecimal): F[Unit] =
        for
          _ <- TransactionRepository.credit(accountId, amount).transact(transactor)
          _ <- logger.info(s"Deposit $amount to the account with $accountId identifier number")
        yield ()

      override def balance(accountId: Int): F[Unit] = Sync[F].unit
