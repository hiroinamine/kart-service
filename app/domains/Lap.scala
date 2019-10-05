package domains

import java.time.{Duration, LocalTime}

/**
  * Define uma volta.
  * @param timestamp horário da volta
  * @param driverId código do piloto
  * @param driverName nome do piloto
  * @param lapNumber número da volta
  * @param lapTime tempo da volta
  * @param averageSpeed média de velocidade da volta
  */
case class Lap(timestamp: LocalTime,
               driverId: String,
               driverName: String,
               lapNumber: Int,
               lapTime: Duration,
               averageSpeed: Double)
