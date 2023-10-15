package lobby.service

import lobby.domain.table.*
import cats.syntax.all.*
import cats.effect.kernel.Ref
import cats.effect.kernel.Sync
import cats.effect.kernel.Clock
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jFactory
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.SelfAwareStructuredLogger

trait Lobby[F[_]]:
  def addTable(request: CreateTable): F[Unit]
  def removeTable(id: String): F[Unit]
  def getTables(): F[List[Table]]

class LobbyInMemory[F[_]: Sync: Clock](
    tables: Ref[F, List[Table]]
)(logger: SelfAwareStructuredLogger[F]) extends Lobby[F]:

  override def addTable(request: CreateTable): F[Unit] =
    for
      table <- Table.fromCreateTable[F](request)
      _     <- tables.update(_.appended(table))
      _     <- logger.info(s"Table with ${table.id} ID was added in the lobby")
    yield ()

  override def removeTable(id: String): F[Unit] =
    for
      _ <- tables.update(_.filterNot(_.id == id))
      _ <- logger.info(s"Table with $id ID was removed from the lobby")
    yield ()

  override def getTables(): F[List[Table]] =
    tables.get

object Lobby:
  def inMemory[F[_]: Sync]: F[Lobby[F]] =
    for
      state  <- Ref.of(List.empty[Table])
      logger <- Slf4jLogger.create[F]
    yield new LobbyInMemory[F](state)(logger)
