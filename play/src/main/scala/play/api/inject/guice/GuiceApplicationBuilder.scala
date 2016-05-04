/*
 * Copyright (C) 2009-2016 Lightbend Inc. <https://www.lightbend.com>
 */
package play.api.inject.guice

import com.google.inject.{ Module => GuiceModule }
import play.api.{ Application, Configuration, Environment, Logger }

/**
 * @see compare-to-play.md
 *
 * A builder for creating Applications using Guice.
 */
final class GuiceApplicationBuilder(
  environment: Environment = Environment.simple(),
  configuration: Configuration = Configuration.empty,
  modules: Seq[GuiceableModule] = Seq.empty,
  overrides: Seq[GuiceableModule] = Seq.empty,
  disabled: Seq[Class[_]] = Seq.empty,
  loadConfiguration: Environment => Configuration = Configuration.load,
  loadModules: (Environment, Configuration) => Seq[GuiceableModule] = GuiceableModule.loadModules) extends GuiceBuilder[GuiceApplicationBuilder](
  environment, configuration, modules, overrides, disabled
) {

  // extra constructor for creating from Java
  def this() = this(environment = Environment.simple())

  /**
   * Set the initial configuration loader.
   * Overrides the default or any previously configured values.
   */
  def loadConfig(loader: Environment => Configuration): GuiceApplicationBuilder =
    copy(loadConfiguration = loader)

  /**
   * Set the initial configuration.
   * Overrides the default or any previously configured values.
   */
  def loadConfig(conf: Configuration): GuiceApplicationBuilder =
    loadConfig(env => conf)

  /**
   * Set the module loader.
   * Overrides the default or any previously configured values.
   */
  def load(loader: (Environment, Configuration) => Seq[GuiceableModule]): GuiceApplicationBuilder =
    copy(loadModules = loader)

  /**
   * Override the module loader with the given modules.
   */
  def load(modules: GuiceableModule*): GuiceApplicationBuilder =
    load((env, conf) => modules)

  /**
   * Create a new Play application Module for an Application using this configured builder.
   */
  override def applicationModule(): GuiceModule = {
    val initialConfiguration = loadConfiguration(environment)
    val appConfiguration = initialConfiguration ++ configuration

    if (appConfiguration.underlying.hasPath("logger")) {
      Logger.warn("Logger configuration in conf files is deprecated and has no effect. Use a logback configuration file instead.")
    }

    val loadedModules = loadModules(environment, appConfiguration)

    copy(configuration = appConfiguration)
      .bindings(loadedModules: _*)
      .createModule
  }

  /**
   * Create a new Play Application using this configured builder.
   */
  def build(): Application = injector.instanceOf[Application]

  /**
   * Internal copy method with defaults.
   */
  private def copy(
    environment: Environment = environment,
    configuration: Configuration = configuration,
    modules: Seq[GuiceableModule] = modules,
    overrides: Seq[GuiceableModule] = overrides,
    disabled: Seq[Class[_]] = disabled,
    loadConfiguration: Environment => Configuration = loadConfiguration,
    loadModules: (Environment, Configuration) => Seq[GuiceableModule] = loadModules): GuiceApplicationBuilder =
    new GuiceApplicationBuilder(environment, configuration, modules, overrides, disabled, loadConfiguration, loadModules)

  /**
   * Implementation of Self creation for GuiceBuilder.
   */
  protected def newBuilder(
    environment: Environment,
    configuration: Configuration,
    modules: Seq[GuiceableModule],
    overrides: Seq[GuiceableModule],
    disabled: Seq[Class[_]]): GuiceApplicationBuilder =
    copy(environment, configuration, modules, overrides, disabled)
}
