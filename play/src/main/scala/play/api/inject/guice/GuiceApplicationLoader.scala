/*
 * Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package play.api.inject.guice

import play.api.{ Application, ApplicationLoader }

/**
 * @see compare-to-play.md
 *
 * An ApplicationLoader that uses Guice to bootstrap the application.
 *
 * Subclasses can override the `builder` and `overrides` methods.
 */
class GuiceApplicationLoader(protected val initialBuilder: GuiceApplicationBuilder) extends ApplicationLoader {

  // empty constructor needed for instantiating via reflection
  def this() = this(new GuiceApplicationBuilder)

  override final def load(context: ApplicationLoader.Context): Application = {
    builder(context).build
  }

  /**
   * Construct a builder to use for loading the given context.
   */
  protected def builder(context: ApplicationLoader.Context): GuiceApplicationBuilder = {
    initialBuilder
      .in(context.environment)
      .loadConfig(context.initialConfiguration)
      .overrides(overrides(context): _*)
  }

  /**
   * Override some bindings using information from the context. The default
   * implementation of this method provides bindings that most applications
   * should include.
   */
  protected def overrides(context: ApplicationLoader.Context): Seq[GuiceableModule] = Nil
}
