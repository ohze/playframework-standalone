lazy val commonSettings = Seq(
  scalaVersion := scala211,
//  TODO crossScalaVersions := V.crossScala,
  Compile / scalacOptions -= "-Xfatal-warnings",
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
    Test / unmanagedSourceDirectories ++= {
      val b = baseDirectory.value
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
    Test/ testOptions += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console"),
    Test / parallelExecution := false
  ).dependsOn(playAlone)

lazy val cacheAlone = project.in(file("play-cache"))
  .settings(commonSettings: _*)
  .settings(
    name := "play-cache-alone",
    libraryDependencies ++= D.cacheAlone,
    Test / testOptions += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console"),
    Test / parallelExecution := false
  ).dependsOn(playAlone, testAlone % "test->test")


lazy val playAloneRoot = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "play-alone-root",
    publishArtifact := false
  )
  .aggregate(playAlone, wsCoreDeps, wsAlone, jdbcAlone, cacheAlone)

inThisBuild(
  Seq(
    versionScheme := Some("semver-spec"),
    developers := List(
      Developer(
        "thanhbv",
        "Bui Viet Thanh",
        "thanhbv@sandinh.net",
        url("https://sandinh.com")
      )
    )
  )
)
