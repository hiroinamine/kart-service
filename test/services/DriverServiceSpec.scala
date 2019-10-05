package services

import java.time.{Duration, LocalTime}

import domains.{Driver, Lap}
import org.scalatestplus.play.PlaySpec
import utils.JsonSerializers

class DriverServiceSpec extends PlaySpec {
  val service = new DriverService()
  val driver = Driver(
    "01",
    "marcela",
    List(
      Lap(LocalTime.of(0, 0, 1), "01", "marcela", 1, Duration.ofMinutes(1), 100.00),
      Lap(LocalTime.of(10, 10, 10), "01", "marcela", 2, Duration.ofSeconds(58), 140.00),
      Lap(LocalTime.of(13, 5, 55), "01", "marcela", 3, Duration.ofMinutes(2), 60.00)
    )
  )

  "DriverService" must {
    "return last lap" in {
      val result = service.lastLap(driver)
      result.timestamp.toString mustBe "13:05:55"
      result.lapNumber mustBe 3
    }

    "calculate total race time" in {
      val result = service.time(driver)
      JsonSerializers.durationToTime(result) mustBe "00:03:58.000"
    }

    "return the fastest lap" in {
      val result = service.fastestLap(driver)
      result.timestamp.toString mustBe "10:10:10"
      result.lapNumber mustBe 2
      JsonSerializers.durationToTime(result.lapTime) mustBe "00:00:58.000"
    }

    "calculate the average speed" in {
      val result = service.avgSpeed(driver)
      result mustBe 100.00
    }

  }
}
