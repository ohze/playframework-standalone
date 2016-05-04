/*
 * Copyright (C) 2009-2016 Lightbend Inc. <https://www.lightbend.com>
 */
package play.api.inject

import akka.actor.ActorSystem
import javax.inject.Provider
import play.api._
import play.api.libs.concurrent.{ExecutionContextProvider, ActorSystemProvider}
import scala.concurrent.ExecutionContext

/**
 * @see compare-to-play.md
 *
 * The Play BuiltinModule.
 *
 * Provides all the core components of a Play application. This is typically automatically enabled by Play for an
 * application.
 */
class BuiltinModule extends Module {
  def bindings(env: Environment, configuration: Configuration): Seq[Binding[_]] = {
    Seq(
      bind[Environment] to env,
      bind[ConfigurationProvider].to(new ConfigurationProvider(configuration)),
      bind[Configuration].toProvider[ConfigurationProvider],

      // Application lifecycle, bound both to the interface, and its implementation, so that Application can access it
      // to shut it down.
      bind[DefaultApplicationLifecycle].toSelf,
      bind[ApplicationLifecycle].to(bind[DefaultApplicationLifecycle]),

      bind[Application].to[DefaultApplication],

      bind[ActorSystem].toProvider[ActorSystemProvider],
      bind[ExecutionContext].toProvider[ExecutionContextProvider]
    )
  }
}

// This allows us to access the original configuration via this
// provider while overriding the binding for Configuration itself.
class ConfigurationProvider(val get: Configuration) extends Provider[Configuration]
