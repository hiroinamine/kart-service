package utils

import java.time.Duration

import domains.{DriverResult, Lap}
import org.json4s.CustomSerializer
import org.json4s.JsonDSL._

object JsonSerializers {
  def durationToTime(duration: Duration): String = {
    val formatter = "%02d:%02d:%02d.%03d"
    formatter.format(duration.toHours, duration.toMinutes % 60, duration.getSeconds % 60, duration.toMillis % 1000)
  }

  object LapSerializer
      extends CustomSerializer[Lap](_ =>
        ({ PartialFunction.empty }, {
          case lap: Lap =>
            ("timestamp" -> lap.timestamp.toString) ~ ("driverId" -> lap.driverId) ~ ("driverName" -> lap.driverName) ~
              ("lapNumber" -> lap.lapNumber) ~ ("lapTime" -> durationToTime(lap.lapTime)) ~ ("averageSpeed" -> lap.averageSpeed)
        }))

  object DriverResultSerializer
      extends CustomSerializer[DriverResult](_ =>
        ({ PartialFunction.empty }, {
          case result: DriverResult =>
            ("position" -> result.position) ~ ("driverId" -> result.driverId) ~ ("driverName" -> result.driver) ~
              ("lapNumber" -> result.lapNumber) ~ ("duration" -> durationToTime(result.duration)) ~
              ("gapTime" -> result.gapTime.map(durationToTime).getOrElse("-"))
        }))
}
