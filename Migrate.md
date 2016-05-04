# Migration guide

### 2.4.x to 2.5.x
see https://www.playframework.com/documentation/2.5.x/Migration25

##### Logging
+ see https://www.playframework.com/documentation/2.5.x/SettingsLogger
+ if you use logback:
```
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-logback" % "2.5.3" % Runtime exclude("com.typesafe.play", "play" + scalaBinaryVersion.value)
  "org.slf4j" % "jul-to-slf4j" % "1.7.21" % Runtime
)
```
