package lobby.domain.table

import auth.domain.user.*
import java.time.Instant
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import cats.syntax.all.*
import cats.effect.kernel.Clock
import cats.Functor

final case class Table(
    id: String,
    configuration: TableConfiguration,
    participants: List[User],
    createdAt: Instant
)

object Table:
  given Encoder[Table] = deriveEncoder[Table]

  def fromCreateTable[F[_]: Functor: Clock](request: CreateTable): F[Table] =
    Clock[F].realTimeInstant.map { instant =>
      Table(request.id, request.configuration, List.empty[User], instant)
    }
