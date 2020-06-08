ThisBuild / scalaVersion := "2.13.1"

ThisBuild / crossScalaVersions += "2.12.11"

ThisBuild / githubWorkflowJavaVersions := Seq("adopt@1.8", "adopt@1.11")
ThisBuild / githubWorkflowPublishTargetBranches := Seq() // Disable "publish" job generation

val specs2Version = "4.9.4"
val http4sVersion = "0.21.4"

lazy val specs2Http4s = (project in file("."))
  .settings(
    name := "specs2-http4s",
    organization := "org.specs2",
    version := specs2Version,
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-matcher" % specs2Version,
      "org.specs2" %% "specs2-cats" % specs2Version,
      "org.http4s" %% "http4s-core" % http4sVersion,
      "org.specs2" %% "specs2-core" % specs2Version % Test
    )
  )
