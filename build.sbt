val playVersion = "2.4.3"

lazy val commonSettings = Seq(
    version := "2.4.2_1",
    scalaVersion := "2.11.7",

    organization := "com.sandinh",

    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-Yinline-warnings"/*, "-optimise"*/),
    javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation"),

    resolvers += Resolver.bintrayRepo("scalaz", "releases"),

    //misc - to mute intellij warning when load sbt project
    dependencyOverrides ++= Set(
        "joda-time"  % "joda-time" % "2.9",
        "org.scala-lang.modules"  %% "scala-parser-combinators" % "1.0.4",
        "org.scala-lang.modules"  %% "scala-xml" % "1.0.5",
        "org.scala-lang" % "scala-reflect" % scalaVersion.value // % Optional
    )
)

def specs2(module: String) = "org.specs2" %% s"specs2-$module" % "3.6.5" % Test

lazy val playAlone = project.in(file("play"))
    .settings(commonSettings: _*)
    .settings(
        name := "play-alone",
        libraryDependencies ++= Seq(
          //play-exceptions have no dependencies
          "com.typesafe.play"     % "play-exceptions" % playVersion,

          //we depends only on object play.api.libs.iteratee.Execution
          //(at play.api.inject.DefaultApplicationLifecycle.stop)
          //which have no dependencies (except scala-library)
          "com.typesafe.play"     %% "play-iteratees" % playVersion intransitive(),

          "com.typesafe"          % "config"          % "1.3.0",
          "com.google.inject"     % "guice"           % "4.0",
          "com.google.inject.extensions" % "guice-assistedinject" % "4.0",
          "com.typesafe.akka"     %% "akka-actor"     % "2.4.0",
          "org.slf4j"             % "slf4j-api"       % "1.7.12"
        )
    )

lazy val wsAlone = project.in(file("play-ws"))
    .settings(commonSettings: _*)
    .settings(
        name := "play-ws-alone",
        unmanagedSourceDirectories in Compile <+= baseDirectory {_ / "play-src-ex"},
        unmanagedSourceDirectories in Test <+= baseDirectory {_ / "play-test" / "src" / "main" / "scala"},
        libraryDependencies ++= Seq(specs2("junit"), specs2("mock"), specs2("matcher-extra"),
          "com.typesafe.play" % "play-netty-utils"  % playVersion % Test,
          "ch.qos.logback"    % "logback-classic"   % "1.1.3"   % Test,
          "org.scala-lang.modules"  %% "scala-parser-combinators" % "1.0.4",
          "org.scala-lang.modules"  %% "scala-xml"  % "1.0.5",
          "com.typesafe.play" %% "play-json"        % playVersion,
          "com.typesafe.play" %% "twirl-api"        % "1.1.1",
          "commons-codec"     % "commons-codec"     % "1.10",
          //[1.9.25](https://github.com/AsyncHttpClient/async-http-client/commits/async-http-client-1.9.25)
          //com.ning.http.client.cookie.Cookie.getMaxAge return `long` => not compatible
          //see https://github.com/AsyncHttpClient/async-http-client/issues/889
          "com.ning"          % "async-http-client" % "1.9.24"
        )
    ).dependsOn(playAlone)

lazy val jdbcAlone = project.in(file("play-jdbc"))
    .settings(commonSettings: _*)
    .settings(
        name := "play-jdbc-alone",
        libraryDependencies ++= Seq(
          //play-jdbc-api depends on `com.typesafe.play %% play`
          //which we need replace by play-alone
          "com.typesafe.play"   %% "play-jdbc-api"  % playVersion intransitive(),
          "tyrex"               % "tyrex"           % "1.0.1",
          "com.zaxxer"          % "HikariCP"        % "2.4.1"
        )
    ).dependsOn(playAlone)

lazy val root = project.in(file("."))
    .settings(commonSettings: _*)
    .settings(
        name := "play-alone-test",
        publishArtifact := false,
        libraryDependencies ++= Seq(specs2("junit"),
            "com.h2database"      %  "h2"           % "1.4.190" % Test,
            "com.typesafe.play"   %% "anorm"        % "2.5.0"   % Test
        ),
        testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console"),
        parallelExecution in Test := false
    ).dependsOn(jdbcAlone)
    .settings(
      projectDependencies := Seq(
        (projectID in jdbcAlone).value
          .exclude("com.typesafe.play", "play_" + scalaBinaryVersion.value)
      )
    )
    .aggregate(playAlone, jdbcAlone, wsAlone)
