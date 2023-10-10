package auth.http

import auth.domain.user.User
import cats.{ Functor, Monad }
import cats.effect.Clock
import cats.syntax.all.*
import io.circe.syntax.EncoderOps
import pdi.jwt.{ JwtAlgorithm, JwtCirce, JwtClaim, JwtOptions }

class JwtProcessor[F[_]: Monad: Clock](secret: String):
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

  def decode(token: String): Option[User] =
    JwtCirce.decodeJson(token, secret, Seq(algorithm)).toOption.flatMap(_.as[User].toOption)

object JwtProcessor:
  def apply[F[_]: Monad: Clock](secret: String): JwtProcessor[F] = new JwtProcessor[F](secret)
