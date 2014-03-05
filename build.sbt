organization := "com.sandinh"

name := "play-jdbc-standalone"

version := "2.0.2_2.2"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-Yinline-warnings"/*, "-optimise"*/)

javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.7", "-target", "1.7", "-Xlint:unchecked", "-Xlint:deprecation")

unmanagedSourceDirectories in Compile <+= baseDirectory{ _ / "play" / "src" / "main" / "scala"}

unmanagedSourceDirectories in Compile <+= baseDirectory{ _ / "play-jdbc" / "src" / "main" / "scala"}

unmanagedSourceDirectories in Compile <+= baseDirectory{ _ / "play-exceptions" / "src" / "main" / "java"}

parallelExecution in Test := false

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies ++= Seq(
    "org.specs2"                    %% "specs2"         % "2.3.8"   % "test",
    "com.h2database"                %  "h2"              % "1.3.175" % "test",
    "com.typesafe.play"             %% "anorm"          % "2.2.2"   % "test",
    "com.github.scala-incubator.io" %% "scala-io-core"  % "0.4.2",
    //binary compatible with 1.0.x. @see https://github.com/typesafehub/config/blob/master/NEWS.md
    "com.typesafe"                  % "config"          % "1.2.0",
    "com.jolbox"                    % "bonecp"          % "0.8.0.RELEASE" exclude("com.google.guava", "guava"),
    //NOTE play 2.2.2 use guava 14.0.1. @see http://repo.typesafe.com/typesafe/releases/com/typesafe/play/play-jdbc_2.10/2.2.2/play-jdbc_2.10-2.2.2.pom
    //but we already use guava 15.0 in production with both play 2.2.1 & play-jdbc-standalone 2.0.1_2.2 :( with no error be found until now
    //and play master branch also use 16.0.1
    //@see https://github.com/playframework/playframework/blob/master/framework/project/Dependencies.scala
    //@see http://code.google.com/p/guava-libraries/wiki/ReleaseHistory
    //so we update guava in play-jdbc-standalone.
    //You - as an user of play-jdbc-standalone - can downgrade guava to 14.0.1 as in play 2.2.2
    "com.google.guava"              % "guava"           % "16.0.1",
    //"com.google.code.findbugs"      % "jsr305"          % "2.0.3" // Needed by guava
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

pomExtra := <url>https://github.com/giabao/play-jdbc-standalone</url>
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
  </developers>
