package services

import java.io.File
import java.time.{Duration, LocalTime}

import domains.{Driver, Lap, Race}
import org.mockito.{ArgumentMatcher, ArgumentMatchers}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.cache.SyncCacheApi
import utils.JsonSerializers

import scala.io.Source

class RaceServiceSpec extends PlaySpec with MockitoSugar {
  val mockCache = mock[SyncCacheApi]
  val service = new RaceService(new LapService, new DriverService, mockCache)

  val drivers = List(
    Driver(
      "01",
      "maria",
      List(
        Lap(LocalTime.of(0, 0, 1), "01", "marcela", 1, Duration.ofMinutes(1), 100.00),
        Lap(LocalTime.of(10, 10, 10), "01", "marcela", 2, Duration.ofSeconds(58), 140.00),
        Lap(LocalTime.of(13, 5, 55), "01", "marcela", 3, Duration.ofMinutes(2), 60.00),
        Lap(LocalTime.of(13, 15, 0), "01", "marcela", 4, Duration.ofMinutes(1), 100.00)
      )
    ),
    Driver(
      "02",
      "pedro",
      List(Lap(LocalTime.of(0, 0, 2), "02", "pedro", 1, Duration.ofMinutes(1), 100.00),
           Lap(LocalTime.of(10, 20, 10), "02", "pedro", 2, Duration.ofSeconds(30), 180.00))
    ),
    Driver(
      "03",
      "ana",
      List(
        Lap(LocalTime.of(0, 0, 1), "03", "ana", 1, Duration.ofMinutes(1), 100.00),
        Lap(LocalTime.of(10, 10, 11), "03", "ana", 2, Duration.ofSeconds(40), 140.00),
        Lap(LocalTime.of(10, 10, 12), "03", "ana", 3, Duration.ofSeconds(50), 110.00),
        Lap(LocalTime.of(10, 10, 14), "03", "ana", 4, Duration.ofMinutes(1), 100.00)
      )
    )
  )
  val race = Race("1", drivers)
  when(mockCache.get[Race]("1")) thenReturn Some(race)
  when(mockCache.get[Race]("not_found")) thenReturn None

  "RaceService" must {
    "return an existent race" in {
      val result = service.find("1")
      result.isDefined mustBe true
      result mustBe Some(race)
    }
    "don't return a race" in {
      val result = service.find("not_found")
      result.isDefined mustBe false
    }
    "return the fastest lap of the entire race" in {
      val result = service.fastestLap(race)
      result.timestamp.toString mustBe "10:20:10"
      result.lapNumber mustBe 2
      result.driverId mustBe "02"
      result.driverName mustBe "pedro"
      JsonSerializers.durationToTime(result.lapTime) mustBe "00:00:30.000"
    }
    "return the result of the race" in {
      val result = service.result(race)
      val driver1 = result.results.find(_.driverId == "01")
      driver1.isDefined mustBe true
      driver1.get.position mustBe Some(2)

      val driver2 = result.results.find(_.driverId == "02")
      driver2.isDefined mustBe true
      driver2.get.position mustBe Some(3)

      val driver3 = result.results.find(_.driverId == "03")
      driver3.isDefined mustBe true
      driver3.get.position mustBe Some(1)
    }
  }
}
