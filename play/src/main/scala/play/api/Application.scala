package play.api

import scala.annotation.implicitNotFound
import scala.reflect.ClassTag

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
