organization := "com.sandinh"

name := "play-jdbc-standalone"

version := "2.0.3"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.11.1", "2.10.4")

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-Yinline-warnings"/*, "-optimise"*/)

javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.7", "-target", "1.7", "-Xlint:unchecked", "-Xlint:deprecation")

unmanagedSourceDirectories in Compile <+= baseDirectory{ _ / "play" / "src" / "main" / "scala"}

unmanagedSourceDirectories in Compile <+= baseDirectory{ _ / "play-jdbc" / "src" / "main" / "scala"}

unmanagedSourceDirectories in Compile <+= baseDirectory{ _ / "play-exceptions" / "src" / "main" / "java"}

parallelExecution in Test := false

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies ++= Seq(
    "org.specs2"                    %% "specs2"         % "2.3.12"     % "test",
    "com.h2database"                %  "h2"             % "1.3.176"    % "test",
    "com.typesafe.play"             %% "anorm"          % "2.3.0-RC2"  % "test",
    "com.typesafe"                  % "config"          % "1.2.1",
    "com.jolbox"                    % "bonecp"          % "0.8.0.RELEASE" exclude("com.google.guava", "guava"),
    "com.google.guava"              % "guava"           % "16.0.1",
    //"com.google.code.findbugs"    % "jsr305"          % "2.0.3" // Needed by guava
    "tyrex"                         % "tyrex"           % "1.0.1"
)
