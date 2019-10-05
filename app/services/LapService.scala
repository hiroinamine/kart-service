package services

import java.time.{Duration, LocalTime}

import domains.{Driver, Lap}
import javax.inject.Inject

class LapService @Inject()() {

  /**
    * Regex para representar o formato do campo de Tempo Volta.
    */
  val TIME_PATTERN = "([0-9]{1,2}):([0-9]{1,2}).([0-9]{1,3})".r

  /**
    * Converte uma linha de log em [[domains.Lap]].
    */
  def parse(lapLog: String): Lap = {
    lapLog.split('\t').map(_.trim) match {
      case Array(timestampStr: String,
                 driverStr: String,
                 lapNumberStr: String,
                 lapTimeStr: String,
                 averageSpeedStr: String) =>
        val timestamp = LocalTime.parse(timestampStr)
        val Array(driverId, driverName) = driverStr.split("-").map(_.trim)
        val lapNumber = lapNumberStr.toInt
        val TIME_PATTERN(minutes, seconds, millis) = lapTimeStr
        val timeLap = Duration.ofMinutes(minutes.toInt).plusSeconds(seconds.toInt).plusMillis(millis.toInt)
        val averageSpeed = averageSpeedStr.replace(",", ".").toDouble
        Lap(timestamp, driverId, driverName, lapNumber, timeLap, averageSpeed)
    }
  }

  /**
    * Retorna uma lista [[domains.Driver]] com base em todas as voltas dos pilotos.
    */
  def drivers(laps: List[Lap]): List[Driver] = {
    laps.groupBy(_.driverId).values.toList.map { laps =>
      val Lap(_, driverId, driverName, _, _, _) = laps.head
      Driver(driverId, driverName, laps)
    }
  }
}
