import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models._
import play.api.db.slick._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  def fakeApp = FakeApplication(additionalConfiguration = inMemoryDatabase())

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "index" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get
      status(home) must equalTo(OK)
    }

    "initData" in new WithApplication(fakeApp) {
      DB.withSession { implicit s: Session =>
        val home = route(FakeRequest(GET, "/initData")).get
        status(home) must equalTo(SEE_OTHER)
        Data.count must beGreaterThanOrEqualTo(0)
      }
    }

    "deleteAll" in new WithApplication(fakeApp) {
      DB.withSession { implicit s: Session =>
        Data.count must beEqualTo(100)
        val home = route(FakeRequest(GET, "/deleteAll")).get
        status(home) must equalTo(SEE_OTHER)
        Data.count must beEqualTo(0)
      }
    }
  }
}
