import sbt._

object Dependencies {
  object Cats {
    val core   = "org.typelevel" %% "cats-core"   % "2.10.0"
    val effect = "org.typelevel" %% "cats-effect" % "3.5.2"
  }

  object Circe {
    val core    = "io.circe" %% "circe-core"    % "0.14.6"
    val generic = "io.circe" %% "circe-generic" % "0.14.6"
    val parser  = "io.circe" %% "circe-parser"  % "0.14.6"
    val refined = "io.circe" %% "circe-refined" % "0.14.6"
  }

  object Http4s {
    val dsl    = "org.http4s" %% s"http4s-dsl"          % "1.0.0-M40"
    val server = "org.http4s" %% s"http4s-ember-server" % "1.0.0-M40"
    val client = "org.http4s" %% s"http4s-ember-client" % "1.0.0-M40"
    val circe  = "org.http4s" %% s"http4s-circe"        % "1.0.0-M40"
  }

  object Doobie {
    val postgres      = "org.tpolecat" %% "doobie-postgres"       % "1.0.0-RC4"
    val postgresCirce = "org.tpolecat" %% "doobie-postgres-circe" % "1.0.0-RC4"
    val hikari        = "org.tpolecat" %% "doobie-hikari"         % "1.0.0-RC4"
  }

  object Refined {
    val core = "eu.timepit" %% "refined"      % "0.11.0"
    val cats = "eu.timepit" %% "refined-cats" % "0.11.0"
  }

  object Ciris {
    val core = "is.cir" %% "ciris" % "3.3.0"
  }

  val log4CatsNoop      = "org.typelevel"        %% "log4cats-noop"      % "2.6.0"
  val flyway            = "org.flywaydb"          % "flyway-core"        % "8.5.13"
  val redis4catsEffects = "dev.profunktor"       %% "redis4cats-effects" % "1.5.0"
  val tsec              = "io.github.jmcardon"   %% "tsec-password"      % "0.4.0"
  val jwtCirce          = "com.github.jwt-scala" %% "jwt-circe"          % "9.4.4"
  val logback           = "ch.qos.logback"        % "logback-classic"    % "1.2.11"
}
