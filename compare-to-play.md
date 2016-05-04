Compare to [play 2.5.3](https://github.com/playframework/playframework/tree/2.5.3/framework/src):

## play-alone
##### play/src/main/resources/reference.conf
+ Remove http, i18n, crypto settings, application.loader
+ remove modules setting: `enabled += "play.api.i18n.I18nModule"`

##### play/src/main/scala/play/api/inject/guice/GuiceApplicationBuilder
 + remove param `global`, defs `global`, `routes`, `router`, `additionalRouter` 
 + `applicationModule`:
 	- remove binding for: `GlobalSettings.Deprecated`, `OptionalSourceMapper`, `WebCommands`
 + remove private classes: `AdditionalRouterProvider`, `FakeRoutes`, `FakeRouterConfig`, `FakeRouterProvider`

##### play/src/main/scala/play/api/inject/guice/GuiceApplicationLoader
in `overrides` method:
+ remove binding for OptionalSourceMapper, WebCommands

##### play/src/main/scala/play/api/inject/BuiltinModule
+ remove binding for:
HttpConfiguration, play.Application (java), Router, HttpExecutionContext,
CryptoConfig, CookieSigner, CSRFTokenSigner, AESCrypter, Crypto, TemporaryFileCreator.
+ remove dynamicBindings for HttpErrorHandler, HttpFilters, HttpRequestHandler, ActionCreator.
+ remove class RoutesProvider

##### play/src/main/scala/play/api/inject/Modules
+ remove logic that try to locate Java modules
  (so we can remove play.{Configuration, Environment} & their dependency java classes)

##### play/src/main/scala/play/api/ApplicationLoader
+ `ApplicationLoader.apply`: always return `new GuiceApplicationLoader`
  (instead of dynamic loading the class in play.application.loader config)
+ remove `abstract class BuiltInComponentsFromContext`

##### play/src/main/scala/play/api/Application
+ `trait Application`: remove isDev, isTest, isProd, global, plugins, plugin, cachedRoutes, routes, requestHandler, errorHandler
+ `class DefaultApplication` remove requestHandler, errorHandler
+ remove class `OptionalSourceMapper` & trait `BuiltInComponents`

##### play/src/main/scala/play/api/Play
+ `start`: commented out global.beforeStart / onStart, routes initialize.
  (In standalone version, we have removed app.routes & app.global)
+ `stop`: commented out: `//app.global.onStop(app)`.
+ remove `routes`, `global`, `langCookieName`, `langCookieSecure`, `langCookieHttpOnly`

##### play/src/main/scala/play/core/system/WebCommands
+ `WebCommands`: remove all members.
This trait is not remove so that `play.api.ApplicationLoader.Context` is compatible with the full Play
+ `DefaultWebCommands`: remove all members.
This trait is not remove so that `play.api.ApplicationLoader.createContext` is compatible with the full Play

##### play/src/main/scala/play/core/ApplicationProvider
+ remove `ApplicationProvider`, `HandleWebCommandSupport`

##### Other files have identical content as in Play source

## play-jdbc-alone
Exactly same as play-jdbc, except the following:
+ remove `bonecp` & `evolutions` settings from `reference.conf`
+ remove BoneConnectionPool initialization case from method `ConnectionPool.fromConfig`
+ remove file `BoneCPModule.scala`
+ not depends on BoneCP

## play-ws-alone
Exactly same as play-ws, except the following:
+ remove play.api.libs.{oauth, openid}
+ remove `play.modules.enabled += "play.api.libs.openid.OpenIDModule"` from `reference.conf`
+ AhcWSRequest: remove deprecated method `streamWithEnumerator`
+ play.api.libs.ws.ahc.Streamed - remove deprecated method `execute2`
+ WSRequest: remove some deprecated methods

## ws-core-deps
+ play/api/http/HttpEntity.scala - remove all `asJava` methods
+ play/api/mvc/ContentTypes.scala - keep only class & object `MultipartFormData`
+ play/api/mvc/Http.scala - keep only class & object `Headers`
+ play/api/mvc/Results.scala - remove all members of trait Results & trait LegacyI18nSupport
+ other classes has identical content as in original play.

## play-exceptions, play-iteratees, play-json, play-datacommons, twirl-api
+ `play-alone` depends on `play-exceptions` and `play-iteratees`
+ `play-ws-alone` also depends on `play-json`, `play-datacommons`, `twirl-api`
+ Those libraries depend on no other play libraries
