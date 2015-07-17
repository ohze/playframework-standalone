play-jdbc-standalone
====================
### What?
This library enable us to use [play-jdbc from playframework 2.4.x](https://github.com/playframework/playframework/blob/2.4.x/framework/src/play-jdbc/src/main/scala/play/api/db/DB.scala) without play itself.

For play version < 2.4 please use [play-jdbc-standalone 2.1.x](http://search.maven.org/#search|ga|1|g%3A%22com.sandinh%22%20play-jdbc-standalone)

### Why?
+ Your code & config (that use play-jdbc-alone) will exactly same as if you [use play](http://www.playframework.com/documentation/2.4.x/ScalaDatabase)
+ So, you can use [anorm](http://www.playframework.com/documentation/2.4.x/ScalaAnorm) or [other database libraries](http://www.playframework.com/documentation/2.4.x/ScalaDatabaseOthers) (exactly) as in a full play app.
+ We are using this library in a db-access library which is used:
    - In some java game servers (in production with millions users at http://sandinh.com) that is NOT a Play application.
      Here, we must use log4j instead of logback. Thanks to play-jdbc-alone which do NOT depend on logback (still depend on slf4j-api)
    - And in the banking module (in an iframe at http://sandinh.com/bank) that IS a Play application.
      Here, we just remove play-jdbc-alone dependency (replaced by the full Play framework)

### How?
+ Get the library from [maven center](http://search.maven.org/#search|ga|1|g%3A%22com.sandinh%22%20play-jdbc-alone)
```
libraryDependencies += "com.sandinh" %% "play-jdbc-alone" % "2.4.2"
```
+ See [DBSpec.scala](https://github.com/giabao/play-jdbc-standalone/blob/master/src/test/scala/play/api/DBSpec.scala)

+ You can also use the plain `play-jdbc` library with `play-alone`:
```
libraryDependencies ++= Seq(
  "com.sandinh"       %% "play-alone" % "2.4.2",
  "com.typesafe.play" %% "play-jdbc" % "2.4.2"
    exclude("com.typesafe.play", "play_" + scalaBinaryVersion.value)
)
```
(but you will get more dependencies)

### Compare to the full Play framework
see [compare-to-play.md](compare-to-play.md)

### Changelogs
see [CHANGES.md](CHANGES.md)

### Licence
This software is licensed under the Apache 2 license:
http://www.apache.org/licenses/LICENSE-2.0

Copyright (C) 2011-2015 Sân Đình (http://sandinh.com)
