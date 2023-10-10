import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalacOptions ++= Seq("-Ykind-projector:underscores")
ThisBuild / scalaVersion := "3.3.1"

lazy val root =
  (project in file("."))
    .settings(
      name := "blackjack-api"
    )

lazy val boot =
  (project in file("boot"))
    .settings(
      name := "blackjack-api-boot",
      libraryDependencies ++= Seq(
        Ciris.core,
        Doobie.hikari,
        Http4s.server,
        flyway
      )
    )
    .dependsOn(auth)

lazy val auth =
  (project in file("auth"))
    .settings(
      name := "blackjack-auth-api",
      libraryDependencies ++= Seq(
        Cats.core,
        Cats.effect,
        Http4s.dsl,
        Http4s.circe,
        Circe.core,
        Circe.generic,
        Doobie.postgres,
        Doobie.postgresCirce,
        tsec,
        jwtCirce,
        logback
      )
    )
