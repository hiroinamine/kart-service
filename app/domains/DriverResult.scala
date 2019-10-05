package domains

import java.time.{Duration, LocalTime}

/**
  * Define o resultado do piloto na corrida.
  * @param position posição do piloto na corrida
  * @param driverId código do piloto
  * @param driver nome do piloto
  * @param lapNumber número de voltas que o piloto fez
  * @param lapTimestamp horário da última volta
  * @param duration duração total da corrida
  * @param gapTime intervalo entre o primeiro colocado
  */
case class DriverResult(position: Option[Int],
                        driverId: String,
                        driver: String,
                        lapNumber: Int,
                        lapTimestamp: LocalTime,
                        duration: Duration,
                        gapTime: Option[Duration])
