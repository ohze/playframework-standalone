organization := "com.sandinh"

name := "play-jdbc-standalone"

version := "2.0.0_2.2"

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
    "org.specs2"                    %% "specs2"         % "2.2.3"   % "test",
    "com.h2database"                % "h2"              % "1.3.173" % "test",
    "com.typesafe.play"             %% "anorm"          % "2.2.0"   % "test",
    "com.github.scala-incubator.io" %% "scala-io-core"  % "0.4.2",
    "com.typesafe"                  % "config"          % "1.0.2",
    "com.jolbox"                    % "bonecp"          % "0.8.0-rc3" exclude("com.google.guava", "guava"),
    "com.google.guava"              % "guava"           % "15.0",
    "tyrex"                         % "tyrex"           % "1.0.1"
)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/giabao/play-jdbc-standalone</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:giabao/play-jdbc-standalone.git</url>
    <connection>scm:git:git@github.com:giabao/play-jdbc-standalone.git</connection>
  </scm>
  <developers>
    <developer>
      <id>giabao</id>
      <name>Gia Bảo</name>
      <email>giabao@sandinh.net</email>
      <organization>Sân Đình</organization>
      <organizationUrl>http://sandinh.com</organizationUrl>
    </developer>
  </developers>)
