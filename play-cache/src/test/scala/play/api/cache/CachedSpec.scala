/*
 * Copyright (C) 2009-2016 Lightbend Inc. <https://www.lightbend.com>
 */
package play.api.cache

import javax.inject._

import play.api.test._

import org.specs2.mutable.Specification

class CachedSpec extends Specification {

  sequential

  "CacheApi" should {
    "get items from cache" in new WithApplication() {
      val defaultCache = app.injector.instanceOf[CacheApi]
      defaultCache.set("foo", "bar")
      defaultCache.get[String]("foo") must beSome("bar")

      defaultCache.set("int", 31)
      defaultCache.get[Int]("int") must beSome(31)

      defaultCache.set("long", 31l)
      defaultCache.get[Long]("long") must beSome(31l)

      defaultCache.set("double", 3.14)
      defaultCache.get[Double]("double") must beSome(3.14)

      defaultCache.set("boolean", true)
      defaultCache.get[Boolean]("boolean") must beSome(true)

      defaultCache.set("unit", ())
      defaultCache.get[Unit]("unit") must beSome(())
    }

    "doesnt give items from cache with wrong type" in new WithApplication() {
      val defaultCache = app.injector.instanceOf[CacheApi]
      defaultCache.set("foo", "bar")
      defaultCache.set("int", 31)
      defaultCache.set("long", 31l)
      defaultCache.set("double", 3.14)
      defaultCache.set("boolean", true)
      defaultCache.set("unit", ())

      defaultCache.get[Int]("foo") must beNone
      defaultCache.get[Long]("foo") must beNone
      defaultCache.get[Double]("foo") must beNone
      defaultCache.get[Boolean]("foo") must beNone
      defaultCache.get[String]("int") must beNone
      defaultCache.get[Long]("int") must beNone
      defaultCache.get[Double]("int") must beNone
      defaultCache.get[Boolean]("int") must beNone
      defaultCache.get[Unit]("foo") must beNone
      defaultCache.get[Int]("unit") must beNone
    }

    "get items from the cache without giving the type" in new WithApplication() {
      val defaultCache = app.injector.instanceOf[CacheApi]
      defaultCache.set("foo", "bar")
      defaultCache.get("foo") must beSome("bar")
      defaultCache.get[Any]("foo") must beSome("bar")

      defaultCache.set("baz", false)
      defaultCache.get("baz") must beSome(false)
      defaultCache.get[Any]("baz") must beSome(false)

      defaultCache.set("int", 31)
      defaultCache.get("int") must beSome(31)
      defaultCache.get[Any]("int") must beSome(31)

      defaultCache.set("unit", ())
      defaultCache.get("unit") must beSome(())
      defaultCache.get[Any]("unit") must beSome(())
    }
  }

  "EhCacheModule" should {
    "support binding multiple different caches" in new WithApplication(
      _.configure("play.cache.bindCaches" -> Seq("custom"))
    ) {
      val component = app.injector.instanceOf[SomeComponent]
      val defaultCache = app.injector.instanceOf[CacheApi]
      component.set("foo", "bar")
      defaultCache.get("foo") must beNone
      component.get("foo") must beSome("bar")
    }
  }

}

class SomeComponent @Inject() (@NamedCache("custom") cache: CacheApi) {
  def get(key: String) = cache.get[String](key)
  def set(key: String, value: String) = cache.set(key, value)
}
