lazy val commonSettings = Seq(
    version := "2.4.2",
    scalaVersion := "2.11.7",

    organization := "com.sandinh",

    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-Yinline-warnings"/*, "-optimise"*/),
    javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation"),

    resolvers += Resolver.bintrayRepo("scalaz", "releases"),

    //misc - to mute intellij warning when load sbt project
    dependencyOverrides ++= Set(
        "org.scala-lang.modules"  %% "scala-parser-combinators" % "1.0.4", // % Optional
        "org.scala-lang" % "scala-reflect" % scalaVersion.value // % Optional
    )
)

lazy val `play-alone` = project.in(file("play"))
    .settings(commonSettings: _*)
    .settings(
        name := "play-alone",
        libraryDependencies ++= Seq(
          //play-exceptions have no dependencies
          "com.typesafe.play"     % "play-exceptions" % "2.4.2",

          //we depends only on object play.api.libs.iteratee.Execution
          //(at play.api.inject.DefaultApplicationLifecycle.stop)
          //which have no dependencies (except scala-library)
          "com.typesafe.play"     %% "play-iteratees" % "2.4.2" intransitive(),

          "com.typesafe"          % "config"          % "1.3.0",
          "com.google.inject"     % "guice"           % "4.0",
          "com.google.inject.extensions" % "guice-assistedinject" % "4.0",
          "com.typesafe.akka"     %% "akka-actor"     % "2.3.12",
          "org.slf4j"             % "slf4j-api"       % "1.7.12"
        )
    )

lazy val `play-jdbc-alone` = project.in(file("play-jdbc"))
    .settings(commonSettings: _*)
    .settings(
        name := "play-jdbc-alone",
        libraryDependencies ++= Seq(
          //play-jdbc-api depends on `com.typesafe.play %% play`
          //which we need replace by play-alone
          "com.typesafe.play"   %% "play-jdbc-api"  % "2.4.2" intransitive(),
          "tyrex"               % "tyrex"           % "1.0.1",
          "com.zaxxer"          % "HikariCP"        % "2.3.9"
        )
    ).dependsOn(`play-alone`)

lazy val root = project.in(file("."))
    .settings(commonSettings: _*)
    .settings(
        name := "play-alone-test",
        publishArtifact := false,
        libraryDependencies ++= Seq(
            "org.specs2"          %% "specs2-junit" % "3.6.2"   % Test,
            "com.h2database"      %  "h2"           % "1.4.187" % Test,
            "com.typesafe.play"   %% "anorm"        % "2.4.0"   % Test
        ),
        testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console"),
        parallelExecution in Test := false
    ).dependsOn(`play-jdbc-alone`)
    .aggregate(`play-alone`, `play-jdbc-alone`)
    .settings(
      projectDependencies := Seq(
        (projectID in `play-jdbc-alone`).value
          .exclude("com.typesafe.play", "play_" + scalaBinaryVersion.value)
      )
    )
