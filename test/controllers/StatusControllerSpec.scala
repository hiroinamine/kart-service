package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

class StatusControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "StatusController GET" should {

    "return information about the API" in {
      val controller = new StatusController(stubControllerComponents())
      val result = controller.get().apply(FakeRequest(GET, "/"))

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
    }
  }
}
