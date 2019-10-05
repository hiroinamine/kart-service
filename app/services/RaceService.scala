package services

import java.io.File
import java.time.Duration
import java.time.temporal.ChronoUnit.MILLIS
import java.util.UUID

import domains.{DriverResult, Lap, Race, RaceResult}
import javax.inject.Inject
import play.api.cache._

import scala.io.Source

class RaceService @Inject()(lapService: LapService, driverService: DriverService, cache: SyncCacheApi) {

  val WINNER_POSITION = 1

  /**
    * Cria uma corrida a partir do log de voltas dos pilotos.
    */
  def create(file: File): Race = {
    val raceId = UUID.randomUUID().toString

    cache.getOrElseUpdate[Race](raceId) {
      val source = Source.fromFile(file)
      val lapLogs = source.getLines()
      val laps = lapLogs.drop(1).map(lapService.parse)

      val drivers = lapService.drivers(laps.toList)
      Race(raceId, drivers)
    }
  }

  /**
    * Busca uma corrida através do seu id.
    */
  def find(id: String): Option[Race] = cache.get[Race](id)

  /**
    * Retorna a lista de resultado de cada piloto na corrida.
    */
  def result(race: Race): RaceResult = {

    /**
      * Define a posição de cada piloto
      */
    val driverResults = race.drivers
      .map { d =>
        val lastLap = driverService.lastLap(d)
        val duration = driverService.time(d)
        DriverResult(None, d.id, d.name, lastLap.lapNumber, lastLap.timestamp, duration, None)
      } sortBy (r => (-r.lapNumber, r.lapTimestamp)) match {
      case winner :: others if winner.lapNumber == 4 =>
        winner.copy(position = Some(WINNER_POSITION)) ::
          others.zipWithIndex.map {
          case (o, index) =>
            val gapTime = MILLIS.between(winner.lapTimestamp, o.lapTimestamp)
            o.copy(position = Some(WINNER_POSITION + index + 1), gapTime = Some(Duration.ofMillis(gapTime)))
        }
      case _ => List() // não houve vencedores
    }

    RaceResult(race.id, driverResults)
  }

  /**
    * Retorna a melhor volta da corrida
    */
  def fastestLap(race: Race): Lap = race.drivers.map(driverService.fastestLap).minBy(_.lapTime)
}
