package play.api

import java.io.File
import play.api.db.BoneCPPlugin
import play.utils.Threads

/**
 * @author giabao
 * created: 2013-10-05 14:45
 * Copyright(c) 2011-2013 sandinh.com
 */
class SimpleApplication(val path: File,
                        val mode: Mode.Mode = Mode.Prod) extends Application{

  def classloader = this.getClass.getClassLoader

  lazy val plugins = Seq(new BoneCPPlugin(this))

  lazy val configuration = Threads.withContextClassLoader(classloader){
    Configuration.load(path, mode)
  }
}
