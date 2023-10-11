package wallet.http

import wallet.service.Wallet
import wallet.domain.TransactionAmount
import cats.syntax.all.*
import cats.effect.Concurrent
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityDecoder.*
import org.http4s.HttpRoutes
import org.http4s.AuthedRoutes
import auth.domain.user.User
import org.http4s.server.Router

class WalletRoutes[F[_]: Concurrent](wallet: Wallet[F]) extends Http4sDsl[F]:
  private val httpRoutes: AuthedRoutes[User, F] = AuthedRoutes.of[User, F] {
    case req @ POST -> Root / "debit" as user =>
      for
        amount <- req.req.as[TransactionAmount]
        _      <- wallet.debit(user.id, amount.amount)
        resp   <- Ok("Withdrawled!")
      yield resp

    case req @ POST -> Root / "credit" as user =>
      for
        amount <- req.req.as[TransactionAmount]
        _      <- wallet.credit(user.id, amount.amount)
        resp   <- Ok("Deposited!")
      yield resp
  }

  final val routes = httpRoutes

object WalletRoutes:
  def apply[F[_]: Concurrent](wallet: Wallet[F]): WalletRoutes[F] =
    new WalletRoutes[F](wallet)
