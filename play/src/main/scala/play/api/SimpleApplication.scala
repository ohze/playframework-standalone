package play.api

import java.io.File
import com.edulify.play.hikaricp.HikariCPPlugin
import play.utils.Threads
import com.typesafe.config.Config

/**
 * @author giabao
 * created: 2013-10-05 14:45
 * Copyright(c) 2011-2013 sandinh.com
 *
 * @param devConfFile Path to application.conf file. This param is only used if mode != Prod.
 */
class SimpleApplication(devConfFile: File, val mode: Mode.Mode, cfg: Config) extends Application {
  /** Configuration will be load using play.api.Configuration#dontAllowMissingConfig() */
  def this(mode: Mode.Mode) = this(null, mode, null)

  /** Constructor for Prod mode.
    * With this constructor, Configuration will be Configuration(cfg) */
  def this(cfg: Config) = this(null, Mode.Prod, cfg)

  /** Constructor for Test mode.
    * Use config in devAppPath/conf/application.conf if config.file & config.resource system properties is not set */
  def this(devAppPath: File) = this(devAppPath, Mode.Test, null)

  def classloader = this.getClass.getClassLoader

  lazy val plugins = Seq(new HikariCPPlugin(this))

  lazy val configuration = Threads.withContextClassLoader(classloader){
    if(cfg != null) Configuration(cfg)
    else if(devConfFile == null) Configuration.load(null, mode)
    else Configuration.load(null, mode, Map("config.file" -> devConfFile.getAbsolutePath))
  }
}
