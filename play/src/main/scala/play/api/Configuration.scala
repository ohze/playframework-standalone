/*
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 */
package play.api

import java.io.File
import com.typesafe.config._
import scala.collection.JavaConverters._
import scala.util.control.NonFatal
import play.utils.PlayIO
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * @author giabao
 * created: 2013-10-05 11:03
 * Copyright(c) 2011-2013 sandinh.com
 *
 * This is a simplified version of the original Play
 */
object Configuration {

  private[this] lazy val dontAllowMissingConfigOptions = ConfigParseOptions.defaults().setAllowMissing(false)

  private[this] lazy val dontAllowMissingConfig = ConfigFactory.load(dontAllowMissingConfigOptions)
  /**
   * loads `Configuration` from config.resource or config.file. If not found default to 'conf/application.conf' in Dev mode
   * @return  configuration to be used
   */
  private[play] def loadDev(appPath: File, devSettings: Map[String, String]): Config = {
    try {
      lazy val file = {
        devSettings.get("config.file").orElse(Option(System.getProperty("config.file")))
          .fold(new File(appPath, "conf/application.conf"))(new File(_))
      }
      val config = Option(System.getProperty("config.resource"))
        .fold(ConfigFactory.parseFileAnySyntax(file))(ConfigFactory.parseResources)

      ConfigFactory.parseMap(devSettings.asJava).withFallback(ConfigFactory.load(config))
    } catch {
      case e: ConfigException => throw configError(e.origin, e.getMessage, Some(e))
    }
  }

  /**
   * Loads a new `Configuration` either from the classpath or from
   * `conf/application.conf` depending on the application's Mode.
   *
   * The provided mode is used if the application is not ready
   * yet, just like when calling this method from `play.api.Application`.
   *
   * Defaults to Mode.Dev
   *
   * @param mode Application mode.
   * @return a `Configuration` instance
   */
  def load(appPath: File, mode: Mode.Mode = Mode.Dev, devSettings: Map[String, String] = Map.empty) = {
    try {
      val currentMode = Play.maybeApplication.fold(mode)(_.mode)
      if (currentMode == Mode.Prod) Configuration(dontAllowMissingConfig) else Configuration(loadDev(appPath, devSettings))
    } catch {
      case e: ConfigException => throw configError(e.origin, e.getMessage, Some(e))
    }
  }

  /**
   * Returns an empty Configuration object.
   */
  val empty = Configuration(ConfigFactory.empty)

  private def configError(origin: ConfigOrigin, message: String, e: Option[Throwable] = None): PlayException = {
    new PlayException.ExceptionSource("Configuration error", message, e.orNull) {
      def line = Option(origin.lineNumber: java.lang.Integer).orNull
      def position = null
      def input = Option(origin.url).map(PlayIO.readUrlAsString).orNull
      def sourceName = Option(origin.filename).orNull
      override def toString = "Configuration error: " + getMessage
    }
  }
}

/**
 * A full configuration set.
 *
 * The underlying implementation is provided by https://github.com/typesafehub/config.
 *
 * @param underlying the underlying Config implementation
 */
case class Configuration(underlying: Config) {
  /**
   * Read a value from the underlying implementation,
   * catching Errors and wrapping it in an Option value.
   */
  private def readValue[T](path: String, v: => T): Option[T] = {
    try {
      Option(v)
    } catch {
      case e: ConfigException.Missing => None
      case NonFatal(e) => throw reportError(path, e.getMessage, Some(e))
    }
  }

  /**
   * Retrieves a configuration value as a `String`.
   *
   * This method supports an optional set of valid values:
   * {{{
   * val config = Configuration.load()
   * val mode = config.getString("engine.mode", Some(Set("dev","prod")))
   * }}}
   *
   * A configuration error will be thrown if the configuration value does not match any of the required values.
   *
   * @param path the configuration key, relative to configuration root key
   * @param validValues valid values for this configuration
   * @return a configuration value
   */
  def getString(path: String, validValues: Option[Set[String]] = None): Option[String] = readValue(path, underlying.getString(path)).map { value =>
    validValues match {
      case Some(values) if values.contains(value) => value
      case Some(values) if values.isEmpty => value
      case Some(values) => throw reportError(path, "Incorrect value, one of " + values.mkString(", ") + " was expected.")
      case None => value
    }
  }

  /**
   * Retrieves a configuration value as an `Int`.
   *
   * For example:
   * {{{
   * val configuration = Configuration.load()
   * val poolSize = configuration.getInt("engine.pool.size")
   * }}}
   *
   * A configuration error will be thrown if the configuration value is not a valid `Int`.
   *
   * @param path the configuration key, relative to the configuration root key
   * @return a configuration value
   */
  def getInt(path: String): Option[Int] = readValue(path, underlying.getInt(path))

  /**
   * Retrieves a configuration value as a `Boolean`.
   *
   * For example:
   * {{{
   * val configuration = Configuration.load()
   * val isEnabled = configuration.getBoolean("engine.isEnabled")
   * }}}
   *
   * A configuration error will be thrown if the configuration value is not a valid `Boolean`.
   * Authorized vales are yes/no or true/false.
   *
   * @param path the configuration key, relative to the configuration root key
   * @return a configuration value
   */
  def getBoolean(path: String): Option[Boolean] = readValue(path, underlying.getBoolean(path))

  /**
   * Retrieves a configuration value as `Milliseconds`.
   *
   * For example:
   * {{{
   * val configuration = Configuration.load()
   * val timeout = configuration.getMilliseconds("engine.timeout")
   * }}}
   *
   * The configuration must be provided as:
   *
   * {{{
   * engine.timeout = 1 second
   * }}}
   */
  def getMilliseconds(path: String): Option[Long] = readValue(path, underlying.getDuration(path, MILLISECONDS))

  /**
   * Retrieves a sub-configuration, i.e. a configuration instance containing all keys starting with a given prefix.
   *
   * For example:
   * {{{
   * val configuration = Configuration.load()
   * val engineConfig = configuration.getConfig("engine")
   * }}}
   *
   * The root key of this new configuration will be ‘engine’, and you can access any sub-keys relatively.
   *
   * @param path the root prefix for this sub-configuration
   * @return a new configuration
   */
  def getConfig(path: String): Option[Configuration] = readValue(path, underlying.getConfig(path)).map(Configuration(_))

  /**
   * Returns sub-keys.
   *
   * For example:
   * {{{
   * val configuration = Configuration.load()
   * val subKeys = configuration.subKeys
   * }}}
   * @return the set of direct sub-keys available in this configuration
   */
  def subKeys: Set[String] = underlying.root().keySet().asScala.toSet

  /**
   * Creates a configuration error for a specific configuration key.
   *
   * For example:
   * {{{
   * val configuration = Configuration.load()
   * throw configuration.reportError("engine.connectionUrl", "Cannot connect!")
   * }}}
   *
   * @param path the configuration key, related to this error
   * @param message the error message
   * @param e the related exception
   * @return a configuration exception
   */
  def reportError(path: String, message: String, e: Option[Throwable] = None): PlayException = {
    Configuration.configError(if (underlying.hasPath(path)) underlying.getValue(path).origin else underlying.root.origin, message, e)
  }

  /**
   * Creates a configuration error for this configuration.
   *
   * For example:
   * {{{
   * val configuration = Configuration.load()
   * throw configuration.globalError("Missing configuration key: [yop.url]")
   * }}}
   *
   * @param message the error message
   * @param e the related exception
   * @return a configuration exception
   */
  def globalError(message: String, e: Option[Throwable] = None): PlayException = {
    Configuration.configError(underlying.root.origin, message, e)
  }

}
