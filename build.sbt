lazy val commonSettings = Seq(
  version := "2.5.3",
  scalaVersion := V.scala,
  organization := "com.sandinh",
  scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-Yinline-warnings"/*, "-optimise"*/),
  javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")
)

lazy val playAlone = project.in(file("play"))
  .settings(commonSettings: _*)
  .settings(
    name := "play-alone",
    libraryDependencies ++= D.playAlone
  )

lazy val wsCoreDeps = project.in(file("ws-core-deps"))
  .settings(commonSettings: _*)
  .settings(
    name := "ws-core-deps",
    libraryDependencies ++= D.wsCoreDeps
  ).dependsOn(playAlone)

lazy val testAlone = project.in(file("play-test-alone"))
  .settings(commonSettings: _*)
  .settings(
    name := "play-test-alone",
    publishArtifact := false,
    unmanagedSourceDirectories in Test <++= baseDirectory { b =>
      Seq("play-server", "play-specs2", "play-test").map(b / _ / "src" / "main" / "scala")
    },
    libraryDependencies ++= D.testAlone
  )
  .dependsOn(wsCoreDeps)

lazy val wsAlone = project.in(file("play-ws"))
  .settings(commonSettings: _*)
  .settings(
    name := "play-ws-alone",
    libraryDependencies ++= D.wsAlone
  ).dependsOn(wsCoreDeps, testAlone % "test->test")

lazy val jdbcAlone = project.in(file("play-jdbc"))
  .settings(commonSettings: _*)
  .settings(
    name := "play-jdbc-alone",
    libraryDependencies ++= D.jdbcAlone(scalaBinaryVersion.value),
    testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console"),
    parallelExecution in Test := false
  ).dependsOn(playAlone)

lazy val playAloneRoot = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "play-alone-root",
    publishArtifact := false
  )
  .aggregate(playAlone, jdbcAlone, wsAlone)
