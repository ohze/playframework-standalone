playframework-standalone
====================
[![CI](https://github.com/ohze/playframework-standalone/actions/workflows/sbt-devops.yml/badge.svg)](https://github.com/ohze/playframework-standalone/actions/workflows/sbt-devops.yml)

### What?
+ `play-alone` is a stripped version of `"com.typesafe.play" %% "play"`
(contains only some core classes, and depends only on some obvious libraries such as com.typesafe:config, akka, slf4j-api)

+ `play-jdbc-alone` is identical to `"com.typesafe.play" %% "play-jdbc"`
except that we removed `bonecp` support (and removed bonecp's transitive dependencies) 

play-jdbc-alone depends on play-alone.

+ `play-ws-alone` is identical to `"com.typesafe.play" %% "play-ws"`
except that we removed play.api.libs.{oauth, openid} packages (and removed the corresponding dependencies)

play-ws-alone depends on play-alone and `ws-core-deps` - which contains some extra classes that also be extracted from `"com.typesafe.play" %% "play"`

Thoses library enable us to write code that use some play (2.5.x) libraries without play itself:
+ [play-jdbc](https://www.playframework.com/documentation/2.5.x/ScalaDatabase)
+ [anorm](https://www.playframework.com/documentation/2.5.x/ScalaAnorm)
+ [play-ws](https://www.playframework.com/documentation/2.5.x/ScalaWS)
+ [play-cache](https://www.playframework.com/documentation/2.5.x/ScalaCache)

### Why?
+ The code & config that use play-jdbc-alone/ play-ws-alone is exactly same as if you use full playframework
+ So, you can use [anorm](http://www.playframework.com/documentation/2.5.x/ScalaAnorm)
 or [other database libraries](http://www.playframework.com/documentation/2.5.x/ScalaDatabaseOthers) (exactly) as in a full play app.
+ We are using play-jdbc-alone, anorm, play-ws-alone in:
    - Some java game servers (in production with millions users at http://sandinh.com) that is NOT a Play application.
    - And in the banking module (in an iframe at http://sandinh.com/bank) that IS a Play application.
      Here, we just remove play-jdbc-alone/ play-ws-alone dependency (replaced by the full Play framework + play-jdbc / play-ws)

### How?
+ Get the library from maven center
    - for play-jdbc-alone:
```
libraryDependencies += "com.sandinh" %% "play-jdbc-alone" % <playAloneVersion>
```
    - for play-ws-alone:
```
libraryDependencies += "com.sandinh" %% "play-ws-alone" % <playAloneVersion>
```

    - for play version < 2.4 please use [play-jdbc-standalone 2.1.x](http://search.maven.org/#search|ga|1|g%3A%22com.sandinh%22%20play-jdbc-standalone)

+ Bootstrap the minimal PlayAlone:
```
com.sandinh.PlayAlone.start()
```

+ Coding & configuring as normal (see the anorm / play-jdbc / play-ws document from [play website](https://www.playframework.com/documentation/2.5.x/ScalaHome))

### Compare to the full Play framework
see [compare-to-play.md](compare-to-play.md)

### Changelogs
see [CHANGES.md](CHANGES.md)

### Licence
This software is licensed under the Apache 2 license:
http://www.apache.org/licenses/LICENSE-2.0

Copyright (C) 2011-2015 Sân Đình (http://sandinh.com)
