package play.api

import org.slf4j.LoggerFactory

/**
 * @author giabao
 * created: 2013-10-17 10:20
 * (c) 2011-2013 sandinh.com
 *
 * This is a simplified version of the original Play
 *
 * Incompatible change in play-jdbc-standalone 2.x:
 * + type play.api.Logger is just an alias to org.slf4j.Logger
 * + The following members in object play.api.Logger is now removed: init, configure, shutdown, ColoredLevel.
 *   If you don't use those members in your code, then ver 2.x is source-compatible with 1.x
 */
object Logger{

  /**
   * The 'application' logger.
   */
  val logger = LoggerFactory.getLogger("application")

  /**
   * Obtains a logger instance.
   *
   * @param name the name of the logger
   * @return a logger
   */
  def apply(name: String): Logger = LoggerFactory.getLogger(name)

  /**
   * Obtains a logger instance.
   *
   * @param clazz a class whose name will be used as logger name
   * @return a logger
   */
  def apply[T](clazz: Class[T]): Logger = LoggerFactory.getLogger(clazz)

}

