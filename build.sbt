organization := "com.sandinh"

name := "play-jdbc-standalone"

version := "2.1.1"

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

unmanagedSourceDirectories in Compile <++= baseDirectory { base => Seq(
    base / "play" / "src" / "main" / "scala",
    base / "play-jdbc" / "src" / "main" / "scala",
    base / "play-exceptions" / "src" / "main" / "java",
    base / "play-hikaricp" / "module-code" / "app"
)}

parallelExecution in Test := false

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies ++= Seq(
    "tyrex"                         % "tyrex"           % "1.0.1",
    "com.zaxxer"                    % "HikariCP-java6"  % "2.2.5",
    "com.typesafe"                  % "config"          % "1.2.1",
    "org.slf4j"                     % "slf4j-api"       % "1.7.7",
    "org.specs2"                    %% "specs2"         % "2.4.11"      % "test",
    "com.h2database"                %  "h2"             % "1.4.182"     % "test",
    "com.typesafe.play"             %% "anorm"          % "2.3.6"       % "test"
)
