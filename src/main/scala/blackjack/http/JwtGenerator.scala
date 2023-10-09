package blackjack.http

import blackjack.domain.user.User
import cats.Functor
import cats.effect.Clock
import cats.syntax.all.*
import io.circe.syntax.EncoderOps
import pdi.jwt.{ JwtAlgorithm, JwtCirce, JwtClaim, JwtOptions }

class JwtGenerator[F[_]: Functor: Clock](secret: String):
  private val algorithm = JwtAlgorithm.HS256

  def generateToken(user: User): F[String] =
    Clock[F].realTimeInstant.map { instant =>
      val claim = JwtClaim(
        content = user.asJson.noSpaces,
        issuedAt = instant.getEpochSecond.some,
        expiration = instant.plusSeconds(648000).getEpochSecond.some
      )

      JwtCirce.encode(claim, secret, algorithm)
    }

object JwtGenerator:
  def apply[F[_]: Functor: Clock](secret: String): JwtGenerator[F] = new JwtGenerator[F](secret)
