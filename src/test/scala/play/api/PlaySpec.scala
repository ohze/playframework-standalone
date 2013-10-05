package play.api

import org.specs2.mutable.Specification
import play.api.db.BoneCPPlugin

/**
 * @author giabao
 * created: 2013-10-05 15:46
 * Copyright(c) 2011-2013 sandinh.com
 */
class PlaySpec extends Specification{
  "Play" should {
    "can start with SimpleApplication" in {
      val app = new SimpleApplication(null)
      Play.start(app)
      Play.maybeApplication must beSome[Application]
    }

    "can load DBPlugin" in {
      val app = new SimpleApplication(null)
      Play.start(app)
      Play.current.plugins must contain((p: Plugin) => p.isInstanceOf[BoneCPPlugin])
    }
  }
}
