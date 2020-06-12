ThisBuild / scalaVersion := "2.13.1"

ThisBuild / crossScalaVersions += "2.12.11"

ThisBuild / githubWorkflowJavaVersions := Seq("adopt@1.8", "adopt@1.11")
ThisBuild / githubWorkflowPublishTargetBranches := Seq() // Disable "publish" job generation

val specs2Version = "4.9.4"
val http4sVersion = "0.21.4"

lazy val specs2Http4s = (project in file("."))
  .settings(
    name := "specs2-http4s",
    description := "specs2 matchers for http4s",
    organization := "org.specs2",
    version := specs2Version,
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-matcher" % specs2Version,
      "org.specs2" %% "specs2-cats" % specs2Version,
      "org.http4s" %% "http4s-core" % http4sVersion,
      "org.specs2" %% "specs2-core" % specs2Version % Test
    ),
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Ywarn-dead-code",
      "-Ywarn-unused",
      "-Xlint",
      "-Xfatal-warnings",
      "-language:higherKinds"
    ),
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, v)) if v <= 12 =>
          Seq("-Ypartial-unification")
        case _ =>
          Nil
      }
    },
    publishSettings
  )

lazy val publishSettings = Seq(
  homepage := Some(url("https://etorreborre.github.io/specs2/")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/etorreborre/specs2-http4s"),
      "git@github.com:etorreborre/specs2-http4s.git"
    )
  ),
  licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  pomExtra := <developers>
    <developer>
      <id>etorreborre</id>
      <name>Eric Torreborre</name>
      <url>https://etorreborre.blogspot.com/</url>
    </developer>
  </developers>,
  publishTo := sonatypePublishToBundle.value
)
