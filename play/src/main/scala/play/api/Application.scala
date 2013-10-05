package play.api

import scala.annotation.implicitNotFound
import scala.reflect.ClassTag
import play.utils.Threads
import java.io.File
import java.lang.reflect.InvocationTargetException

/** This is a simplified version of the original Play code */
trait WithDefaultConfiguration {
  self: Application =>

  protected lazy val initialConfiguration = Threads.withContextClassLoader(self.classloader) {
    Configuration.load(path, mode/*, this match {
      case dev: DevSettings => dev.devSettings
      case _ => Map.empty
    }*/)
  }

  private lazy val fullConfiguration = initialConfiguration //global.onLoadConfig(initialConfiguration, path, classloader, mode)

  def configuration: Configuration = fullConfiguration
}

trait WithDefaultPlugins {
  self: Application =>

  private[api] def pluginClasses: Seq[String] = {

    import scalax.io.JavaConverters._
    import scala.collection.JavaConverters._

    val PluginDeclaration = """([0-9_]+):(.*)""".r

    val pluginFiles = self.classloader.getResources("play.plugins").asScala.toList ++ self.classloader.getResources("conf/play.plugins").asScala.toList

    pluginFiles.distinct.map { plugins =>
      plugins.asInput.string.split("\n").map(_.replaceAll("#.*$", "").trim).filterNot(_.isEmpty).map {
        case PluginDeclaration(priority, className) => (priority.toInt, className)
      }
    }.flatten.sortBy(_._1).map(_._2)

  }

  /**
   * The plugins list used by this application.
   *
   * Plugin classes must extend play.api.Plugin and are automatically discovered
   * by searching for all play.plugins files in the classpath.
   *
   * A play.plugins file contains a list of plugin classes to be loaded, and sorted by priority:
   *
   * {{{
   * 100:play.api.i18n.MessagesPlugin
   * 200:play.api.db.DBPlugin
   * 250:play.api.cache.BasicCachePlugin
   * 300:play.db.ebean.EbeanPlugin
   * 400:play.db.jpa.JPAPlugin
   * 500:play.api.db.evolutions.EvolutionsPlugin
   * 1000:play.api.libs.akka.AkkaPlugin
   * 10000:play.api.GlobalPlugin
   * }}}
   *
   * @see play.api.Plugin
   */
  lazy val plugins: Seq[Plugin] = Threads.withContextClassLoader(classloader) {

    pluginClasses.map { className =>
      try {
        val plugin = classloader.loadClass(className).getConstructor(classOf[Application]).newInstance(this).asInstanceOf[Plugin]
        if (plugin.enabled) Some(plugin) else { Play.logger.debug("Plugin [" + className + "] is disabled"); None }
      } catch {
        case e: java.lang.NoSuchMethodException =>
          throw new PlayException("Cannot load plugin",
            s"Could not find an appropriate constructor to instantiate plugin [$className]." +
             "All Play plugins must define a constructor that accepts a single argument of type play.api.Application." +
             "Note that in play-jdbc-standalone, we only support Scala plugins!")
        case e: InvocationTargetException => throw new PlayException(
          "Cannot load plugin",
          "An exception occurred during Plugin [" + className + "] initialization",
          e.getTargetException)
        case e: PlayException => throw e
        case e: ThreadDeath => throw e
        case e: VirtualMachineError => throw e
        case e: Throwable => throw new PlayException(
          "Cannot load plugin",
          "Plugin [" + className + "] cannot been instantiated.",
          e)
      }
    }.flatten
  }
}

/**
 * @author giabao
 * created: 2013-10-05 10:43
 * Copyright(c) 2011-2013 sandinh.
 *
 * This is a simplified version of the original Play
 * @todo add an implement for this trait
 */
@implicitNotFound(msg = "You do not have an implicit Application in scope. If you want to bring the current running Application into context, just add import play.api.Play.current")
trait Application {
  /**
   * The absolute path hosting this application, mainly used by the `getFile(path)` helper method
   */
  def path: File

  /**
   * The application's classloader
   */
  def classloader: ClassLoader

  /**
   * `Dev`, `Prod` or `Test`
   */
  def mode: Mode.Mode

  def configuration: Configuration
  def plugins: Seq[Plugin]

  /**
   * Retrieves a plugin of type `T`.
   *
   * For example, retrieving the DBPlugin instance:
   * {{{
   * val dbPlugin = application.plugin(classOf[DBPlugin])
   * }}}
   *
   * @tparam T the plugin type
   * @param  pluginClass the plugin’s class
   * @return the plugin instance, wrapped in an option, used by this application
   * @throws Error if no plugins of type `T` are loaded by this application
   */
  def plugin[T](pluginClass: Class[T]): Option[T] =
    plugins.find(p => pluginClass.isAssignableFrom(p.getClass)).map(_.asInstanceOf[T])

  /**
   * Retrieves a plugin of type `T`.
   *
   * For example, to retrieve the DBPlugin instance:
   * {{{
   * val dbPlugin = application.plugin[DBPlugin].map(_.api).getOrElse(sys.error("problem with the plugin"))
   * }}}
   *
   * @tparam T the plugin type
   * @return The plugin instance used by this application.
   * @throws Error if no plugins of type T are loaded by this application.
   */
  def plugin[T](implicit ct: ClassTag[T]): Option[T] = plugin(ct.runtimeClass).asInstanceOf[Option[T]]
}
