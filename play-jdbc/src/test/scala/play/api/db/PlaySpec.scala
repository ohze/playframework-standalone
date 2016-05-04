package play.api.db

import com.sandinh.PlayAlone
import org.specs2.mutable.Specification
import play.api.{Application, Play}

/**
 * @author giabao
 * created: 2013-10-05 15:46
 * Copyright(c) 2011-2013 sandinh.com
 */
class PlaySpec extends Specification{
  "Play standalone" should {
    "can start with Application.simple" in {
      PlayAlone.start()
      Play.maybeApplication must beSome[Application]
    }

    "can load HikariCPModule" in {
      PlayAlone.start()
      Play.current.injector.instanceOf[ConnectionPool] must beAnInstanceOf[HikariCPConnectionPool]
    }
  }
}
