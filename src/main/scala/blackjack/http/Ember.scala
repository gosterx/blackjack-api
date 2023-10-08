package blackjack.http

import cats.syntax.all.*
import cats.effect.kernel.{ Async, Resource }
import cats.effect.std.Console
import com.comcast.ip4s.*
import fs2.io.net.Network
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.http4s.server.defaults.Banner
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.noop.NoOpFactory

object Ember:
  private def showBanner[F[_]: Console](server: Server): F[Unit] =
    Console[F].println(s"\n${Banner.mkString("\n")}\nHTTP Server started at ${server.address}")

  private def make[F[_]: Async](httpApp: HttpApp[F], port: Port) =
    given LoggerFactory[F] = NoOpFactory[F]
    EmberServerBuilder.default[F].withHost(host"0.0.0.0").withPort(port).withHttpApp(httpApp)

  def default[F[_]: Async: Console](httpApp: HttpApp[F], port: Port): Resource[F, Server] =
    make(httpApp, port).build.evalTap(showBanner[F])
