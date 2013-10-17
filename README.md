play-jdbc-standalone
====================
### What?
This library enable us to use [play-jdbc from playframework 2.2](https://github.com/playframework/playframework/blob/2.2.x/framework/src/play-jdbc/src/main/scala/play/api/db/DB.scala) without play itself.

### Why?
+ Your code & config (that use play-jdbc-standalone) will exactly same as if you [use play](http://www.playframework.com/documentation/2.2.x/ScalaDatabase)
+ So, you can use [anorm](http://www.playframework.com/documentation/2.2.x/ScalaAnorm) or [other database libraries](http://www.playframework.com/documentation/2.2.x/ScalaDatabaseOthers) (exactly) as in a full play app.

### How?
see [DBSpec.scala](https://github.com/giabao/play-jdbc-standalone/blob/master/src/test/scala/play/api/DBSpec.scala)

### Changelogs
Note: `_2.2` in version number is the compatible version of (original) Play
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
