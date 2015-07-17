package com.sandinh

import play.api._

object PlayAlone {
  /**
   * @param configFile The path to application.conf file.
   *                   Pass this param (!= null) will have same affect as setting `config.file` System property.
   *                   This param (if != null) has higher priority than `config.file` System property.
   */
  def start(configFile: String = null, mode: Mode.Mode = Mode.Test): Unit = {
    val env = Environment.simple(mode = mode)

    val initialSettings =
      if (configFile == null) Map.empty[String, AnyRef]
      else Map("config.file" -> configFile)

    val context = ApplicationLoader.createContext(env, initialSettings)
    val loader = ApplicationLoader(context)
    val app = loader.load(context)

    Play.start(app)
  }
}
