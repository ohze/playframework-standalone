/*
 * Copyright (C) 2009-2014 Typesafe Inc. <http://www.typesafe.com>
 */
package play.utils

import java.io._
import scala.io.Codec
import java.net.URL

/**
 * IO utilites for internal use by Play projects.
 *
 * This is intentionally not public API.
 */
private[play] object PlayIO {

  /**
   * Read the given stream into a byte array.
   *
   * Does not close the stream.
   */
  def readStream(stream: InputStream): Array[Byte] = {
    val buffer = new Array[Byte](8192)
    var len = stream.read(buffer)
    val out = new ByteArrayOutputStream()
    while (len != -1) {
      out.write(buffer, 0, len)
      len = stream.read(buffer)
    }
    out.toByteArray
  }

  /**
   * Read the given stream into a String.
   *
   * Does not close the stream.
   */
  def readStreamAsString(stream: InputStream)(implicit codec: Codec): String = {
    new String(readStream(stream), codec.name)
  }

  /**
   * Read the URL as a String.
   */
  def readUrlAsString(url: URL)(implicit codec: Codec): String = {
    val is = url.openStream()
    try {
      readStreamAsString(is)
    } finally {
      closeQuietly(is)
    }
  }

  /**
   * Close the given closeable quietly.
   *
   * Logs any IOExceptions encountered.
   */
  def closeQuietly(closeable: Closeable) = {
    try {
      if (closeable != null) {
        closeable.close()
      }
    } catch {
      case e: IOException => play.api.Play.logger.warn("Error closing stream", e)
    }
  }
}
