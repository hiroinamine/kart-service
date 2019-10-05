package services

import java.time.Duration

import domains.{Driver, Lap}
import javax.inject.Inject

class DriverService @Inject()() {

  /**
    * Retorna a última volta de um piloto.
    */
  def lastLap(driver: Driver): Lap = driver.laps.maxBy(_.lapNumber)

  /**
    * Calcula o tempo total de corrida de um piloto.
    */
  def time(driver: Driver): Duration = driver.laps.map(_.lapTime).reduce((t1, t2) => t1.plus(t2))

  /**
    * Retorna a volta mais rápida de um piloto.
    */
  def fastestLap(driver: Driver): Lap = driver.laps.minBy(_.lapTime)

  /**
    * Calcula a velocidade média da corrida de um piloto.
    */
  def avgSpeed(driver: Driver): Double = {
    val speed = driver.laps.map(_.averageSpeed)
    speed.sum / speed.length
  }
}
