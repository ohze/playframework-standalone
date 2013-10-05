package play.api

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
 * @todo init _currentApp
 */
object Play {
  /*
 * A general purpose logger for Play. Intended for internal usage.
 */
  private[play] val logger = Logger("play")

  private[play] var _currentApp: Application = _

  /**
   * Optionally returns the current running application.
   */
  def maybeApplication: Option[Application] = Option(_currentApp)
}
