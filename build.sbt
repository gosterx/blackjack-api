import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalacOptions ++= Seq("-Ykind-projector:underscores")

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "blackjack-api",
    libraryDependencies ++= Seq(
      Cats.core,
      Cats.effect,
      Http4s.dsl,
      Http4s.server,
      Http4s.circe,
      Circe.core,
      Circe.generic,
      Circe.parser,
      Circe.refined,
      Doobie.postgres,
      Doobie.postgresCirce,
      Doobie.hikari,
      Refined.core,
      Refined.cats,
      Ciris.core,
      log4CatsNoop,
      flyway,
      redis4catsEffects,
      tsec,
      jwtCirce,
      logback % Runtime
    )
  )
