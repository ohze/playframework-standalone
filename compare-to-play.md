## play-jdbc-alone
Exactly same as play-jdbc, except the following:
    + remove `bonecp` & `evolutions` settings from `reference.conf`
    + remove BoneConnectionPool initialization case from method `ConnectionPool.fromConfig`
    + remove file `BoneCPModule.scala`
    + not depends on BoneCP

## play-exceptions
`play-alone` depends on `play-exceptions` which have no dependencies (except scala-library)

## play-iteratees
`play-alone` depends only on `object play.api.libs.iteratee.Execution` in `play-iteratees`
`iteratee.Execution` is used at `play.api.inject.DefaultApplicationLifecycle.stop`
`object iteratee.Execution` have no dependencies (except scala-library)

## play-alone
Compare to [play 2.4.x branch at 18 Jul 2015](https://github.com/playframework/playframework/tree/5978b27/framework/src/play):

##### play/src/main/resources/reference.conf
    + Remove http, i18n, crypto settings
    + remove modules setting: `enabled += "play.api.i18n.I18nModule"`

##### play/src/main/scala/play/api/inject/guice/GuiceApplicationBuilder
 	+ remove `global`
 	+ `applicationModule`:
 		- remove binding for: `GlobalSettings`, `OptionalSourceMapper`, `WebCommands`
 		- remove: `Logger.configure(environment)`

##### play/src/main/scala/play/api/inject/guice/GuiceApplicationLoader
in `overrides` method:
 	+ remove binding for OptionalSourceMapper, WebCommands

##### play/src/main/scala/play/api/inject/ApplicationLifecycle
is simplified by removing (so we can remove many java classes in play):
	+ addStopHook(java.util.concurrent.Callable)

##### play/src/main/scala/play/api/inject/BuiltinModule
remove binding for:
  	+ HttpConfiguration
  	+ Router
  	+ Plugins
  	+ CryptoConfig
  	+ Crypto,
  	+ play.Application (java)

##### play/src/main/scala/play/api/inject/Modules
	+ remove logic that try to locate Java modules
	+ so we can remove play.{Configuration, Environment} & their dependency java classes

##### play/src/main/scala/play/api/Application
    + `trait Application`: remove global, plugins, plugin, cachedRoutes, routes, requestHandler, errorHandler
    + `class DefaultApplication` remove plugins, requestHandler, errorHandler
    + remove class `OptionalSourceMapper` & trait `BuiltInComponents`

##### play/src/main/scala/play/api/ApplicationLoader
    + `ApplicationLoader.apply`: always return `new GuiceApplicationLoader`
       (instead of dynamic loading the class in play.application.loader config)
    + remove `abstract class BuiltInComponentsFromContext`

##### play/src/main/scala/play/api/Logger
simplified by removing (so that `play-alone` do NOT have `logback` dependency):
    + init
    + configure
    + shutdown
    + ColoredLevel

##### play/src/main/scala/play/api/Play
    + remove `XML`m `routes`, `global`, `langCookieName`, `langCookieSecure`, `langCookieHttpOnly`
    + `start`: commented out routes & plugins initialize logic.
      In standalone version, we don't need app.routes
      and play-jdbc-standalone do NOT support the deprecated play Plugin system
      (use Play Module instead)
    + `stop`: commented out plugins stopping logic.

##### play/src/main/scala/play/core/system/WebCommands
    + `WebCommands`: remove all members.
      This trait is not remove so that `play.api.ApplicationLoader.Context` is compatible with the full Play
    + `DefaultWebCommands`: remove all members.
      This trait is not remove so that `play.api.ApplicationLoader.createContext` is compatible with the full Play

##### play/src/main/scala/play/core/ApplicationProvider
    + remove `ApplicationProvider`, `HandleWebCommandSupport`

##### Other files are have identical content as in Play source
