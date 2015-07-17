organization := "com.sandinh"

name := "play-jdbc-standalone"

version := "2.2.0-SNAPSHOT"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-Yinline-warnings"/*, "-optimise"*/)

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console")

unmanagedSourceDirectories in Compile <++= baseDirectory { base => Seq(
    base / "play" / "src" / "main" / "scala",
    base / "play-jdbc" / "src" / "main" / "scala",
    base / "play-exceptions" / "src" / "main" / "java",
    base / "play-hikaricp" / "module-code" / "app"
)}

parallelExecution in Test := false

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies ++= Seq(
    //FIXME
    //"tyrex"                         % "tyrex"           % "1.0.1",
    //"com.zaxxer"                    % "HikariCP-java6"  % "2.2.5",
    "com.typesafe"                  % "config"          % "1.3.0",
    //"org.slf4j"                     % "slf4j-api"       % "1.7.12",
    //"org.specs2"                    %% "specs2"         % "2.4.15"      % Test,
    //"com.h2database"                %  "h2"             % "1.4.184"     % Test,
    "com.typesafe.play"             %% "anorm"          % "2.4.0"       % Test
)

//misc - to mute intellij warning when load sbt project
dependencyOverrides ++= Set(
    "org.scala-lang.modules"  %% "scala-parser-combinators" % "1.0.4", // % Optional
    "org.scala-lang" % "scala-reflect" % "2.11.7" // % Optional
)
