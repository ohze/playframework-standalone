package play.api

import java.io.File
import play.api.db.BoneCPPlugin
import play.utils.Threads

/**
 * @author giabao
 * created: 2013-10-05 14:45
 * Copyright(c) 2011-2013 sandinh.com
 *
 * @param devAppPath Path to a directory that contain conf/application.conf file. This param is only used if mode != Prod.
 */
class SimpleApplication(devAppPath: File, val mode: Mode.Mode = Mode.Test) extends Application{
  /** Constructor for Prod mode.
    * Configuration will be load using play.api.Configuration#dontAllowMissingConfig() */
  def this() = this(null, Mode.Prod)

  def classloader = this.getClass.getClassLoader

  lazy val plugins = Seq(new BoneCPPlugin(this))

  lazy val configuration = Threads.withContextClassLoader(classloader){
    Configuration.load(devAppPath, mode)
  }
}
