/*
 * Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package play.api

import org.slf4j.{ LoggerFactory, Logger => Slf4jLogger }

/**
 * Typical logger interface.
 */
trait LoggerLike {

  /**
   * The underlying SLF4J Logger.
   */
  val logger: Slf4jLogger

  /**
   * The underlying SLF4J Logger.
   */
  lazy val underlyingLogger = logger

  /**
   * `true` if the logger instance is enabled for the `TRACE` level.
   */
  def isTraceEnabled = logger.isTraceEnabled

  /**
   * `true` if the logger instance is enabled for the `DEBUG` level.
   */
  def isDebugEnabled = logger.isDebugEnabled

  /**
   * `true` if the logger instance is enabled for the `INFO` level.
   */
  def isInfoEnabled = logger.isInfoEnabled

  /**
   * `true` if the logger instance is enabled for the `WARN` level.
   */
  def isWarnEnabled = logger.isWarnEnabled

  /**
   * `true` if the logger instance is enabled for the `ERROR` level.
   */
  def isErrorEnabled = logger.isErrorEnabled

  /**
   * Logs a message with the `TRACE` level.
   *
   * @param message the message to log
   */
  def trace(message: => String) {
    if (logger.isTraceEnabled) logger.trace(message)
  }

  /**
   * Logs a message with the `TRACE` level.
   *
   * @param message the message to log
   * @param error the associated exception
   */
  def trace(message: => String, error: => Throwable) {
    if (logger.isTraceEnabled) logger.trace(message, error)
  }

  /**
   * Logs a message with the `DEBUG` level.
   *
   * @param message the message to log
   */
  def debug(message: => String) {
    if (logger.isDebugEnabled) logger.debug(message)
  }

  /**
   * Logs a message with the `DEBUG` level.
   *
   * @param message the message to log
   * @param error the associated exception
   */
  def debug(message: => String, error: => Throwable) {
    if (logger.isDebugEnabled) logger.debug(message, error)
  }

  /**
   * Logs a message with the `INFO` level.
   *
   * @param message the message to log
   */
  def info(message: => String) {
    if (logger.isInfoEnabled) logger.info(message)
  }

  /**
   * Logs a message with the `INFO` level.
   *
   * @param message the message to log
   * @param error the associated exception
   */
  def info(message: => String, error: => Throwable) {
    if (logger.isInfoEnabled) logger.info(message, error)
  }

  /**
   * Logs a message with the `WARN` level.
   *
   * @param message the message to log
   */
  def warn(message: => String) {
    if (logger.isWarnEnabled) logger.warn(message)
  }

  /**
   * Logs a message with the `WARN` level.
   *
   * @param message the message to log
   * @param error the associated exception
   */
  def warn(message: => String, error: => Throwable) {
    if (logger.isWarnEnabled) logger.warn(message, error)
  }

  /**
   * Logs a message with the `ERROR` level.
   *
   * @param message the message to log
   */
  def error(message: => String) {
    if (logger.isErrorEnabled) logger.error(message)
  }

  /**
   * Logs a message with the `ERROR` level.
   *
   * @param message the message to log
   * @param error the associated exception
   */
  def error(message: => String, error: => Throwable) {
    if (logger.isErrorEnabled) logger.error(message, error)
  }

}

/**
 * A Play logger.
 *
 * @param logger the underlying SL4FJ logger
 */
class Logger(val logger: Slf4jLogger) extends LoggerLike

/**
 * updated by giabao <giabao@sandinh.net>
 *
 * This is a simplified version of the original Play with incompatible changes:
 * + The following members in object play.api.Logger is removed: init, configure, shutdown, ColoredLevel.
 *   If you don't use those members in your code, then this simplified Logger is compatible with full play's one.
 *   (else then there will be an compile error)
 */
object Logger extends LoggerLike {

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
  def apply(name: String): Logger = new Logger(LoggerFactory.getLogger(name))

  /**
   * Obtains a logger instance.
   *
   * @param clazz a class whose name will be used as logger name
   * @return a logger
   */
  def apply[T](clazz: Class[T]): Logger = new Logger(LoggerFactory.getLogger(clazz.getName.stripSuffix("$")))
}
