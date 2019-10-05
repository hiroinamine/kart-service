package domains

/**
  * Define o piloto.
  * @param id código do piloto
  * @param name nome do piloto
  * @param laps voltas do piloto
  */
case class Driver(id: String, name: String, laps: List[Lap])
