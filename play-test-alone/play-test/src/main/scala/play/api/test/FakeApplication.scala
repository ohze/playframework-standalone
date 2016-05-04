/*
 * Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package play.api.test

import akka.actor.ActorSystem
import play.api._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject._
import scala.concurrent.Future
import play.api.Application

/**
 * A Fake application.
 *
 * @param path The application path
 * @param classloader The application classloader
 * @param additionalConfiguration Additional configuration
 */
case class FakeApplication(
    override val path: java.io.File = new java.io.File("."),
    override val classloader: ClassLoader = classOf[FakeApplication].getClassLoader,
    additionalConfiguration: Map[String, _ <: Any] = Map.empty) extends Application {

  private val app: Application = new GuiceApplicationBuilder()
    .in(Environment(path, classloader, Mode.Test))
    .configure(additionalConfiguration)
    .build()

  override def mode: Mode.Mode = app.mode
  override def configuration: Configuration = app.configuration
  override def actorSystem: ActorSystem = app.actorSystem
  override def stop(): Future[Unit] = app.stop()
  override def injector: Injector = app.injector
}
