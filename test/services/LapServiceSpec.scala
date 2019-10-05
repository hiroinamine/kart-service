package services

import java.time.{Duration, LocalTime}

import domains.Lap
import org.scalatestplus.play.PlaySpec
import utils.JsonSerializers

class LapServiceSpec extends PlaySpec {
  val service = new LapService()
  "LapService" must {
    "parse a lap log text to Lap" in {
      val result = service.parse("01:59:30.100\t052 - Joao\t1\t30:00.190\t100.10")
      result.timestamp.toString mustBe "01:59:30.100"
      result.driverId mustBe "052"
      result.driverName mustBe "Joao"
      result.lapNumber mustBe 1
      JsonSerializers.durationToTime(result.lapTime) mustBe "00:30:00.190"
      result.averageSpeed mustBe 100.10
    }

    "group laps by driver" in {
      val laps = List(
        Lap(LocalTime.now(), "01", "ronaldo", 1, Duration.ZERO, 0),
        Lap(LocalTime.now(), "02", "joao", 1, Duration.ZERO, 0),
        Lap(LocalTime.now(), "01", "ronaldo", 2, Duration.ZERO, 0)
      )
      val result = service.drivers(laps)
      result.length mustBe 2
      val driver01 = result.find(_.id == "01")
      driver01.isDefined mustBe true
      driver01.get.laps.length mustBe 2

      val driver02 = result.find(_.id == "02")
      driver02.isDefined mustBe true
      driver02.get.laps.length mustBe 1
    }
  }
}
