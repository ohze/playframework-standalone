/*
 * Copyright (C) 2009-2016 Lightbend Inc. <https://www.lightbend.com>
 */
package play.api.mvc

import play.api.mvc.MultipartFormData._

/**
 * Multipart form data body.
 */
case class MultipartFormData[A](dataParts: Map[String, Seq[String]], files: Seq[FilePart[A]], badParts: Seq[BadPart]) {

  /**
   * Extract the data parts as Form url encoded.
   */
  def asFormUrlEncoded: Map[String, Seq[String]] = dataParts

  /**
   * Access a file part.
   */
  def file(key: String): Option[FilePart[A]] = files.find(_.key == key)
}

/**
 * Defines parts handled by Multipart form data.
 */
object MultipartFormData {

  /**
   * A part.
   *
   * @tparam A the type that file parts are exposed as.
   */
  sealed trait Part[+A]

  /**
   * A data part.
   */
  case class DataPart(key: String, value: String) extends Part[Nothing]

  /**
   * A file part.
   */
  case class FilePart[A](key: String, filename: String, contentType: Option[String], ref: A) extends Part[A]

  /**
   * A part that has not been properly parsed.
   */
  case class BadPart(headers: Map[String, String]) extends Part[Nothing]

  /**
   * Emitted when the multipart stream can't be parsed for some reason.
   */
  case class ParseError(message: String) extends Part[Nothing]

  /**
   * The multipart/form-data parser buffers many things in memory, including data parts, headers, file names etc.
   *
   * Some buffer limits apply to each element, eg, there is a buffer for headers before they are parsed.  Other buffer
   * limits apply to all in memory data in aggregate, this includes data parts, file names, part names.
   *
   * If any of these buffers are exceeded, this will be emitted.
   */
  case class MaxMemoryBufferExceeded(message: String) extends Part[Nothing]
}
