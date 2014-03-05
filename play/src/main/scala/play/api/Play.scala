/*
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 */
package play.api

import play.utils.Threads
import scala.util.control.NonFatal

/** Application mode, either `DEV`, `TEST`, or `PROD`. */
object Mode extends Enumeration {
  type Mode = Value
  val Dev, Test, Prod = Value
}

/**
 * @author giabao
 * created: 2013-10-05 12:20
 * Copyright(c) 2011-2013 sandinh.com
 *
 * This is a simplified version of the original Play
 *
 * High-level API to access Play global features.
 *
 * Note that this API depends on a running application.
 * You can import the currently running application in a scope using:
 * {{{
 * import play.api.Play.current
 * }}}
 */
object Play {

  /*
   * A general purpose logger for Play. Intended for internal usage.
   */
  private[play] val logger = Logger("play")

  /**
   * Optionally returns the current running application.
   */
  def maybeApplication: Option[Application] = Option(_currentApp)

  /**
   * Implicitly import the current running application in the context.
   *
   * Note that by relying on this, your code will only work properly in
   * the context of a running application.
   */
  implicit def current: Application = maybeApplication.getOrElse(sys.error("There is no started application"))

  private[play] var _currentApp: Application = _

  /**
   * Starts this application.
   *
   * @param app the application to start
   */
  def start(app: Application) {

    // First stop previous app if exists
    stop()

    _currentApp = app

    //@giabao: commented out the following line. In standalone version, we don't need app.routes
    //app.routes
    Threads.withContextClassLoader(classloader(app)) {
      app.plugins.foreach(_.onStart())
    }

    app.mode match {
      case Mode.Test =>
      case mode => logger.info("Application started (" + mode + ")")
    }

  }

  /**
   * Stops the current application.
   */
  def stop() {
    Option(_currentApp).map { app =>
      Threads.withContextClassLoader(classloader(app)) {
        app.plugins.reverse.foreach { p =>
          try { p.onStop() } catch { case NonFatal(e) => logger.warn("Error stopping plugin", e) }
        }
      }
    }
    _currentApp = null
  }

  /**
   * Returns the current application classloader.
   */
  def classloader(implicit app: Application): ClassLoader = app.classloader
}
