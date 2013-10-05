package play.api

import java.io.File

/**
 * @author giabao
 * created: 2013-10-05 14:45
 * Copyright(c) 2011-2013 sandinh.com
 */
class SimpleApplication(val path: File,
                        val mode: Mode.Mode = Mode.Prod)
  extends Application with WithDefaultConfiguration with WithDefaultPlugins{

  def classloader = this.getClass.getClassLoader
}
