organization := "sandinh"

name := "play-jdbc-standalone"

version := "2.2.0"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-Yinline-warnings"/*, "-optimise"*/)

javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.7", "-target", "1.7", "-Xlint:unchecked", "-Xlint:deprecation")

//note: boncp 0.8.0-rc3 upgraded to guava 15.0
//https://github.com/wwadge/bonecp/commit/9585251054b052ed6005299ee360332a716468d7#diff-600376dffeb79835ede4a0b285078036

libraryDependencies ++= Seq(
    "com.jolbox"        % "bonecp"  % "0.8.0-rc3" exclude("com.google.guava", "guava"),
    "com.google.guava"  % "guava"   % "15.0",
    "tyrex"             % "tyrex"   % "1.0.1"
)
