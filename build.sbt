organization := "com.sandinh"

name := "play-jdbc-standalone"

version := "2.0.5"

scalaVersion := "2.11.4"

crossScalaVersions := Seq("2.11.4", "2.10.4")

scalacOptions ++= Seq("-encoding", "UTF-8", "-target:jvm-1.7", "-deprecation", "-unchecked", "-feature", "-Yinline-warnings"/*, "-optimise"*/)

javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.7", "-target", "1.7", "-Xlint:unchecked", "-Xlint:deprecation")

javacOptions ++= {
    val JavaVersion = """1\.(\d+)""".r
    val JavaVersion(v) = sys.props("java.specification.version")
    v.toInt match {
        case 7 => Nil
        case 8 =>
            val rtJar7 = Seq("C:/Program Files (x86)/Java/jre7/lib/rt.jar", "C:/Program Files/Java/jre7/lib/rt.jar")
                .find(p => Path(new java.io.File(p)).exists)
                .getOrElse(sys.error("Cant find rt.jar in JRE 7 for cross-compiling java sources from JDK 8"))
            Seq("-bootclasspath", rtJar7, "-extdirs", "")
        case _     => sys.error("Need java >= 1.7")
    }
}

//@see https://github.com/etorreborre/specs2/issues/283
lazy val root = (project in file(".")) disablePlugins plugins.JUnitXmlReportPlugin

testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console")

unmanagedSourceDirectories in Compile <+= baseDirectory{ _ / "play" / "src" / "main" / "scala"}

unmanagedSourceDirectories in Compile <+= baseDirectory{ _ / "play-jdbc" / "src" / "main" / "scala"}

unmanagedSourceDirectories in Compile <+= baseDirectory{ _ / "play-exceptions" / "src" / "main" / "java"}

parallelExecution in Test := false

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies ++= Seq(
    "org.specs2"                    %% "specs2"         % "2.4.11"      % "test",
    "com.h2database"                %  "h2"             % "1.4.182"     % "test",
    "com.typesafe.play"             %% "anorm"          % "2.3.6"       % "test",
    "com.typesafe"                  % "config"          % "1.2.1",
    "com.jolbox"                    % "bonecp"          % "0.8.0.RELEASE" exclude("com.google.guava", "guava"),
    "com.google.guava"              % "guava"           % "16.0.1",
    //"com.google.code.findbugs"    % "jsr305"          % "2.0.3" // Needed by guava
    "tyrex"                         % "tyrex"           % "1.0.1"
)
