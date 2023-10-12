package wallet.http

import wallet.service.Wallet
import wallet.domain.Amount
import wallet.domain.WalletError.*
import wallet.domain.WalletError.encoder
import cats.syntax.all.*
import cats.effect.Concurrent
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityDecoder.*
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.HttpRoutes
import org.http4s.AuthedRoutes
import auth.domain.user.User
import org.http4s.server.Router
import org.http4s.server.middleware.ErrorHandling
import cats.data.Kleisli
import cats.data.OptionT
import io.circe.syntax.EncoderOps

class WalletRoutes[F[_]: Concurrent](wallet: Wallet[F]) extends Http4sDsl[F]:
  private val httpRoutes: AuthedRoutes[User, F] = AuthedRoutes.of[User, F] {
    case req @ POST -> Root / "debit" as user =>
      for
        amount <- req.req.as[Amount]
        _      <- wallet.debit(user.id, amount.amount)
        resp   <- Ok("Withdrawled!")
      yield resp

    case req @ POST -> Root / "credit" as user =>
      for
        amount <- req.req.as[Amount]
        _      <- wallet.credit(user.id, amount.amount)
        resp   <- Ok("Deposited!")
      yield resp

    case GET -> Root / "balance" as user =>
      for
        amount <- wallet.balance(user.id)
        resp   <- Ok(amount)
      yield resp
  }

  final val routes = httpRoutes.recoverWith {
    case err @ InsufficientFunds => Kleisli { _ =>
        OptionT.liftF(BadRequest(err.asJson))
      }
  }

object WalletRoutes:
  def apply[F[_]: Concurrent](wallet: Wallet[F]): WalletRoutes[F] =
    new WalletRoutes[F](wallet)
