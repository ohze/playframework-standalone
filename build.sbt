organization := "sandinh"

name := "play-jdbc-standalone"

version := "1.0.0_2.2" //2.2 is version of Play

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-Yinline-warnings"/*, "-optimise"*/)

javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.7", "-target", "1.7", "-Xlint:unchecked", "-Xlint:deprecation")

unmanagedSourceDirectories in Compile := Seq(
    file("play/src/main/scala"),
    file("play-jdbc/src/main/scala"),
    file("play-exceptions/src/main/java")
)

parallelExecution in Test := false

//note: boncp 0.8.0-rc3 upgraded to guava 15.0
//https://github.com/wwadge/bonecp/commit/9585251054b052ed6005299ee360332a716468d7#diff-600376dffeb79835ede4a0b285078036

libraryDependencies ++= Seq(
    "org.specs2"                    % "specs2_2.10"     % "2.2.2"   % "test",
    "com.h2database"                % "h2"              % "1.3.173" % "test",
    "com.typesafe.play"             %% "anorm"          % "2.2.0"   % "test",
    "ch.qos.logback"                % "logback-classic" % "1.0.13",
    "org.slf4j"                     % "jul-to-slf4j"    % "1.7.5",
    "com.github.scala-incubator.io" %% "scala-io-core"  % "0.4.2",
    "com.typesafe"                  % "config"          % "1.0.2",
    "com.jolbox"                    % "bonecp"          % "0.8.0-rc3" exclude("com.google.guava", "guava"),
    "com.google.guava"              % "guava"           % "15.0",
    "tyrex"                         % "tyrex"           % "1.0.1"
)
