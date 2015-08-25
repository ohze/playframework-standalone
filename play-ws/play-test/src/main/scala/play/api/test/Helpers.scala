/*
 * Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package play.api.test

import scala.language.reflectiveCalls

import play.api._
import play.api.mvc._
import play.api.http._

import play.api.libs.iteratee._
import play.api.libs.json.{ Json, JsValue }

import play.twirl.api.Content

import scala.concurrent.Await
import scala.concurrent.duration._

import scala.concurrent.Future
import akka.util.Timeout

/**
 * Helper functions to run tests.
 */
trait PlayRunners extends HttpVerbs {

  /**
   * Executes a block of code in a running application.
   */
  def running[T](app: Application)(block: => T): T = {
    PlayRunners.mutex.synchronized {
      try {
        Play.start(app)
        block
      } finally {
        Play.stop(app)
      }
    }
  }

  /**
   * The port to use for a test server. Defaults to 19001. May be configured using the system property
   * testserver.port
   */
  lazy val testServerPort = Option(System.getProperty("testserver.port")).map(_.toInt).getOrElse(19001)

  /**
   * Constructs a in-memory (h2) database configuration to add to a FakeApplication.
   */
  def inMemoryDatabase(name: String = "default", options: Map[String, String] = Map.empty[String, String]): Map[String, String] = {
    val optionsForDbUrl = options.map { case (k, v) => k + "=" + v }.mkString(";", ";", "")

    Map(
      ("db." + name + ".driver") -> "org.h2.Driver",
      ("db." + name + ".url") -> ("jdbc:h2:mem:play-test-" + scala.util.Random.nextInt + optionsForDbUrl)
    )
  }

}

object PlayRunners {
  /**
   * This mutex is used to ensure that no two tests that set the global application can run at the same time.
   */
  private[play] val mutex: AnyRef = new AnyRef()
}

trait Writeables

trait DefaultAwaitTimeout {

  /**
   * The default await timeout.  Override this to change it.
   */
  implicit def defaultAwaitTimeout: Timeout = 20.seconds

  /**
   * How long we should wait for something that we expect *not* to happen, e.g.
   * waiting to make sure that a channel is *not* closed by some concurrent process.
   *
   * NegativeTimeout is a separate type to a normal Timeout because we'll want to
   * set it to a lower value. This is because in normal usage we'll need to wait
   * for the full length of time to show that nothing has happened in that time.
   * If the value is too high then we'll spend a lot of time waiting during normal
   * usage. If it is too low, however, we may miss events that occur after the
   * timeout has finished. This is a necessary tradeoff.
   *
   * Where possible, tests should avoid using a NegativeTimeout. Tests will often
   * know exactly when an event should occur. In this case they can perform a
   * check for the event immediately rather than using using NegativeTimeout.
   */
  case class NegativeTimeout(t: Timeout)
  implicit val defaultNegativeTimeout = NegativeTimeout(200.millis)

}

trait FutureAwaits {
  self: DefaultAwaitTimeout =>

  import java.util.concurrent.TimeUnit

  /**
   * Block until a Promise is redeemed.
   */
  def await[T](future: Future[T])(implicit timeout: Timeout): T = Await.result(future, timeout.duration)

  /**
   * Block until a Promise is redeemed with the specified timeout.
   */
  def await[T](future: Future[T], timeout: Long, unit: TimeUnit): T =
    Await.result(future, Duration(timeout, unit))

}

trait EssentialActionCaller {
  self: Writeables =>
}

trait RouteInvokers extends EssentialActionCaller {
  self: Writeables =>
}

trait ResultExtractors {
  self: HeaderNames with Status =>

  /**
   * Extracts the Content-Type of this Content value.
   */
  def contentType(of: Content)(implicit timeout: Timeout): String = of.contentType

  /**
   * Extracts the content as String.
   */
  def contentAsString(of: Content)(implicit timeout: Timeout): String = of.body

  /**
   * Extracts the content as bytes.
   */
  def contentAsBytes(of: Content)(implicit timeout: Timeout): Array[Byte] = of.body.getBytes

  /**
   * Extracts the content as Json.
   */
  def contentAsJson(of: Content)(implicit timeout: Timeout): JsValue = Json.parse(of.body)
}

object Helpers extends PlayRunners
  with HeaderNames
  with Status
  with HttpProtocol
  with DefaultAwaitTimeout
  with ResultExtractors
  with Writeables
  with EssentialActionCaller
  with RouteInvokers
  with FutureAwaits
