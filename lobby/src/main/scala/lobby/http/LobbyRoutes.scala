package lobby.http

import auth.domain.user.*
import lobby.domain.*
import lobby.domain.table.*
import cats.syntax.all.*
import cats.effect.Concurrent
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityDecoder.*
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.AuthedRoutes
import lobby.service.Lobby

class LobbyRoutes[F[_]: Concurrent](lobby: Lobby[F]) extends Http4sDsl[F]:
  private val httpRoutes: AuthedRoutes[User, F] = AuthedRoutes.of[User, F] {
    case GET -> Root as user =>
      for
        tables <- lobby.getTables()
        lobbyView = LobbyView(tables)
        resp <- Ok(lobbyView)
      yield resp

    case req @ POST -> Root / "tables" as user =>
      for
        createTable <- req.req.as[CreateTable]
        _           <- lobby.addTable(createTable)
        resp        <- Created()
      yield resp

    case DELETE -> Root / "tables" / tableId as user =>
      for
        _    <- lobby.removeTable(tableId)
        resp <- NoContent()
      yield resp
  }

  final val routes = httpRoutes

object LobbyRoutes:
  def apply[F[_]: Concurrent](lobby: Lobby[F]): LobbyRoutes[F] =
    new LobbyRoutes[F](lobby)
