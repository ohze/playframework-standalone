import sbt._

object V {
  val play = "2.5.3"
  val playIteratee = "2.6.0"
  val guice = "4.0"
  val config = "1.3.0"
  val akka = "2.4.4"
  val j8compat = "0.7.0" //0.8.0-RC1
  val scalaXml = "1.0.5"
  val slf4j = "1.7.21"
  val xerces = "2.11.0"

  val asyncHttp = "2.0.2"
  val twirlApi = "1.1.1"
  val logback = "1.1.7"

  val jdbcdslog = "1.0.6.2"
  val tyrex = "1.0.1"
  val hikari = "2.4.6"

  val anorm = "2.5.1"
  val h2 = "1.4.191"
  val commonsCodec = "1.10"
  val ehcache = "2.6.11"
  def specs2(module: String) = "org.specs2" %% s"specs2-$module" % "3.6.6" % Test
}

object D {
  val playAlone = Seq(
    //play-exceptions & play-iteratees depend on no other play modules
    "com.typesafe.play"             % "play-exceptions"       % V.play,
    "com.typesafe.play"             %% "play-iteratees"       % V.playIteratee,
    "com.typesafe"                  % "config"                % V.config,
    "com.google.inject"             % "guice"                 % V.guice,
    "com.google.inject.extensions"  % "guice-assistedinject"  % V.guice,
    "org.scala-lang.modules"        %% "scala-java8-compat"   % V.j8compat,
    "org.scala-lang.modules"        %% "scala-xml"            % V.scalaXml,
    "com.typesafe.akka"             %% "akka-actor"           % V.akka,
    "com.typesafe.akka"             %% "akka-stream"          % V.akka,
    "com.typesafe.akka"             %% "akka-slf4j"           % V.akka,
    "org.slf4j"                     % "slf4j-api"             % V.slf4j,
    "xerces"                        % "xercesImpl"            % V.xerces
  )

  val wsCoreDeps = Seq(
    "com.typesafe.play" %% "play-json"        % V.play,
    "com.typesafe.play" %% "twirl-api"        % V.twirlApi
  )

  val testAlone = Seq(
    V.specs2("core"),
    "commons-codec"     % "commons-codec"     % V.commonsCodec % Test
  )

  val wsAlone = Seq(
    "org.asynchttpclient" % "async-http-client" % V.asyncHttp,
    V.specs2("junit"), V.specs2("mock"), V.specs2("matcher-extra"),
    "ch.qos.logback"    % "logback-classic"   % V.logback     % Test
  )

  def jdbcAlone(scalaBinaryVersion: String) = Seq(
    "com.googlecode.usc"  % "jdbcdslog"       % V.jdbcdslog,
    //play-jdbc-api depends on `com.typesafe.play %% play` which we need replace by play-alone
    //not use `intransitive()` because `publishMavenStyle := true`
    "com.typesafe.play"   %% "play-jdbc-api"  % V.play exclude ("com.typesafe.play", s"play_$scalaBinaryVersion"),
    "tyrex"               % "tyrex"           % V.tyrex,
    "com.zaxxer"          % "HikariCP"        % V.hikari,
    V.specs2("junit"),
    "com.h2database"      %  "h2"     % V.h2    % Test,
    "com.typesafe.play"   %% "anorm"  % V.anorm % Test
  )

  def cacheAlone = Seq(
    "net.sf.ehcache"    % "ehcache-core"    % V.ehcache,
    "javax.inject"      % "javax.inject"    % "1",
    "com.typesafe.play" % "play-streams_2.11" % V.play,
    "com.typesafe.play" %% "play-specs2"    % V.play % Test,
    "ch.qos.logback"    % "logback-classic" % V.logback % Test,
    "ch.qos.logback"    % "logback-core"    % V.logback % Test,
    V.specs2("junit"),
    V.specs2("core"),
    V.specs2("mock")
  )
}
