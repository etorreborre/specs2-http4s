ThisBuild / scalaVersion := "2.13.6"

ThisBuild / crossScalaVersions += "2.12.14"

val specs2Version = "4.12.2"
val http4sVersion = "1.0.0-M23"

lazy val specs2Http4s = (project in file(".")).
  enablePlugins(GitBranchPrompt, GitVersioning).
  settings(
    name := "specs2-http4s",
    description := "specs2 matchers for http4s",
    organization := "org.specs2",
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
    releaseSettings,
  )

lazy val releaseSettings: Seq[Setting[_]] = Seq(
  ThisBuild / versionScheme := Some("early-semver"),
  ThisBuild / githubWorkflowArtifactUpload := false,
  ThisBuild / githubWorkflowBuild := Seq(
    WorkflowStep.Sbt(
      name = Some("Build and test 🔧"),
      commands = List("testOnly -- xonly exclude ci timefactor 3"))),
  ThisBuild / githubWorkflowTargetTags ++= Seq(SPECS2_HTTP4S+"*"),
  ThisBuild / githubWorkflowPublishTargetBranches := Seq(RefPredicate.StartsWith(Ref.Tag(SPECS2_HTTP4S))),
  ThisBuild / githubWorkflowPublish := Seq(
    WorkflowStep.Sbt(
      name = Some("Release to Sonatype 📇"),
      commands = List("ci-release"),
      env = Map(
        "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
        "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
        "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
        "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
      )
    ),
  ),
  organization := "org.specs2",
  homepage := Some(url("https://github.com/etorreborre/specs2-http4s")),
  licenses := List("MIT" -> url("http://www.apache.org/licenses/MIT")),
  developers := List(
    Developer(
      "etorreborre",
      "Eric Torreborre",
      "etorreborre@yahoo.com",
      url("https://github.com/etorreborre")
    )
  ),
  ThisBuild / git.gitTagToVersionNumber := { tag: String => if (tag matches SPECS2_HTTP4S+".*") Some(tag.replace(SPECS2_HTTP4S, "")) else None },
  ThisBuild / dynverTagPrefix := SPECS2_HTTP4S)

val SPECS2_HTTP4S = "SPECS2-HTTP4S-"
