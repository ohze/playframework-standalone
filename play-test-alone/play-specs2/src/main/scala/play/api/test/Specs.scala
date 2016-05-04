/*
 * Copyright (C) 2009-2016 Lightbend Inc. <https://www.lightbend.com>
 */
package play.api.test

import org.specs2.execute.{AsResult, Result}
import org.specs2.mutable.Around
import org.specs2.specification.Scope
import play.api.inject.guice.{ GuiceApplicationBuilder, GuiceApplicationLoader }
import play.api.{ Application, ApplicationLoader, Environment, Mode }

// NOTE: Do *not* put any initialisation code in the below classes, otherwise delayedInit() gets invoked twice
// which means around() gets invoked twice and everything is not happy.  Only lazy vals and defs are allowed, no vals
// or any other code blocks.

/**
 * Used to run specs within the context of a running application loaded by the given `ApplicationLoader`.
 *
 * @param applicationLoader The application loader to use
 * @param context The context supplied to the application loader
 */
abstract class WithApplicationLoader(applicationLoader: ApplicationLoader = new GuiceApplicationLoader(), context: ApplicationLoader.Context = ApplicationLoader.createContext(new Environment(new java.io.File("."), ApplicationLoader.getClass.getClassLoader, Mode.Test))) extends Around with Scope {
  implicit lazy val app = applicationLoader.load(context)
  def around[T: AsResult](t: => T): Result = {
    Helpers.running(app)(AsResult.effectively(t))
  }
}

/**
 * Used to run specs within the context of a running application.
 *
 * @param app The fake application
 */
abstract class WithApplication(val app: Application = GuiceApplicationBuilder().build()) extends Around with Scope {

  def this(builder: GuiceApplicationBuilder => GuiceApplicationBuilder) {
    this(builder(GuiceApplicationBuilder()).build())
  }

  implicit def implicitApp = app
  implicit def implicitMaterializer = app.materializer
  override def around[T: AsResult](t: => T): Result = {
    Helpers.running(app)(AsResult.effectively(t))
  }
}
