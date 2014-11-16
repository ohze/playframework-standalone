play-jdbc-standalone
====================
### What?
This library enable us to use [play-jdbc from playframework 2.2+](https://github.com/playframework/playframework/blob/2.3.x/framework/src/play-jdbc/src/main/scala/play/api/db/DB.scala) without play itself.

### Why?
+ Your code & config (that use play-jdbc-standalone) will exactly same as if you [use play](http://www.playframework.com/documentation/2.3.x/ScalaDatabase)
+ So, you can use [anorm](http://www.playframework.com/documentation/2.3.x/ScalaAnorm) or [other database libraries](http://www.playframework.com/documentation/2.3.x/ScalaDatabaseOthers) (exactly) as in a full play app.

### How?
see [DBSpec.scala](https://github.com/giabao/play-jdbc-standalone/blob/master/src/test/scala/play/api/DBSpec.scala)

### Changelogs
Note: `_2.2` in version number (from <= v2.0.2_2.2) is the compatible version of (original) Play

##### v2.1.0

1. use [HikariCP](https://github.com/brettwooldridge/HikariCP) instead of BoneCP (BoneCP author himself has [said](https://github.com/wwadge/bonecp) the library is deprecated, use Hikari),

2. remove AutoCleanConnection. see: https://github.com/playframework/playframework/issues/2445#issuecomment-37495865

3. add class play.api.Logger (in previous version, this type is just an alias to org.slf4j.Logger).
 object play.api.Logger is now also an LoggerLike as in the full play version - so, we can use it directly as: Logger.info(..)

4. play-jdbc config now require configs for `user` & `password` even if
you use a driver previously do NOT need those configs - ex when use h2 driver. see [diff](https://github.com/giabao/play-jdbc-standalone/commit/567674680ba6adb939d780542f61420643a98d28#diff-240affed5ecb8cbeca8d5ce26e5a15d3) of src/test/resources/conf/application.conf

##### v2.0.5
+ review code with play 2.3.2
+ update scala 2.11.2

##### v2.0.4
+ sync code with play 2.3.0-RC2.
+ update scala 2.11.1

##### v2.0.3
+ sync code with play 2.3.0-RC1.
 As a result, play-jdbc-standalone now do NOT depend on scala-io-core
+ this version is binary compatible with v2.0.2_2.2.
 So, it is compatible with both play 2.2 & play 2.3.
 So, I remove `_2.2` from version number.
+ cross compile to scala 2.10 & 2.11
+ use [sbt-sonatype](https://github.com/xerial/sbt-sonatype)

##### v2.0.2_2.2
+ sync code with [play 2.2.2](https://github.com/playframework/playframework/tree/2.2.2)
(I use [Beyond Compare](http://www.scootersoftware.com/download.php) to compare & sync playframework/framework/src with play-jdbc-standalone

+ I also update com.typesafe:config from v1.0.2 to 1.2.0 which is binary compatible.
@see https://github.com/typesafehub/config/blob/master/NEWS.md

+ NOTE play 2.2.2 [use guava 14.0.1](http://repo.typesafe.com/typesafe/releases/com/typesafe/play/play-jdbc_2.10/2.2.2/play-jdbc_2.10-2.2.2.pom)
but we already use guava 15.0 in production with both play 2.2.1 & play-jdbc-standalone 2.0.1_2.2 :( with no error be found until now,
and [play master branch](https://github.com/playframework/playframework/blob/master/framework/project/Dependencies.scala) also use 16.0.1
@see http://code.google.com/p/guava-libraries/wiki/ReleaseHistory
so we update guava in play-jdbc-standalone.
You - as an user of play-jdbc-standalone - can downgrade guava to 14.0.1 as in play 2.2.2

##### v2.0.1_2.2
Update bonecp 0.8.0.RELEASE

##### v2.0.0_2.2
Version 2 remove dependencies: logback, jul-to-slf4j.
This permit us to use play-jdbc-standalone in an existing system that use another logging framework.

Incompatible change in play-jdbc-standalone 2.x:

+ remove object play.utils.Colors
+ type play.api.Logger is just an alias to org.slf4j.Logger
+ The following members in object play.api.Logger is now removed: init, configure, shutdown, ColoredLevel.
  
  If you don't use those members in your code, then ver 2.x is source-compatible with 1.x.
  (But now, you need manually config your logger)

This version also add som convenient constructors to [SimpleApplication](https://github.com/giabao/play-jdbc-standalone/blob/master/play/src/main/scala/play/api/SimpleApplication.scala)

##### v1.0.0_2.2
First stable release

### Licence
This software is licensed under the Apache 2 license:
http://www.apache.org/licenses/LICENSE-2.0

Copyright 2013 Sân Đình (http://sandinh.com)
