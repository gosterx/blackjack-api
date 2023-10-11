import Dependencies._

scalafmtOnCompile := true

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalacOptions ++= Seq("-Ykind-projector:underscores")
ThisBuild / scalaVersion := "3.3.1"

lazy val root =
  (project in file("."))
    .settings(
      name := "blackjack-api"
    )
    .aggregate(boot, auth, wallet)

lazy val boot =
  (project in file("boot"))
    .settings(
      name := "blackjack-api-boot",
      libraryDependencies ++= Seq(
        Http4s.client,
        Ciris.core,
        Doobie.hikari,
        flyway
      )
    )
    .dependsOn(auth, wallet)

lazy val auth =
  (project in file("auth"))
    .settings(
      name := "blackjack-auth-api",
      libraryDependencies ++= Seq(
        Cats.core,
        Cats.effect,
        Http4s.dsl,
        Http4s.circe,
        Http4s.server,
        Circe.core,
        Circe.generic,
        Doobie.postgres,
        Doobie.postgresCirce,
        tsec,
        jwtCirce,
        logback
      )
    )

lazy val wallet =
  (project in file("wallet"))
    .settings(
      name := "blackjack-wallet-api"
    )
    .dependsOn(auth)
