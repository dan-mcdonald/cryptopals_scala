val scala3Version = "3.3.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "cryptopals",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    scalacOptions := {Seq(
      // "-explain",
    )},

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0-M8" % Test
  )
