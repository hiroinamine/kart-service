package controllers

import domains.RaceResult
import javax.inject.Inject
import org.json4s.native.Serialization
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{DriverService, RaceService}
import utils.JsonSerializers.{DriverResultSerializer, LapSerializer}

class RaceController @Inject()(cc: ControllerComponents, raceService: RaceService, driverService: DriverService)
    extends AbstractController(cc) {

  implicit val formats = org.json4s.DefaultFormats + LapSerializer + DriverResultSerializer

  def create = Action(parse.multipartFormData) { request =>
    request.body.files.headOption match {
      case Some(file) =>
        val race = raceService.create(file.ref)
        Ok(Serialization.write(race)).as(JSON)
      case _ => BadRequest(Json.obj("msg" -> "Expect some parameter"))
    }
  }

  def get(id: String) = Action {
    raceService.find(id) match {
      case Some(race) =>
        raceService.result(race) match {
          case RaceResult(_, List()) => Ok(Json.obj("msg" -> "No winner"))
          case result                => Ok(Serialization.write(result)).as(JSON)
        }
      case None =>
        NotFound(Json.obj("msg" -> s"Race id '$id' not found"))
    }
  }

  def fastestLap(id: String) = Action {
    raceService.find(id) match {
      case Some(race) =>
        val result = raceService.fastestLap(race)
        Ok(Serialization.write(result)).as(JSON)
      case None =>
        NotFound(Json.obj("msg" -> s"Race id '$id' not found"))
    }
  }

  def fastestLapByDriver(id: String) = Action {
    raceService.find(id) match {
      case Some(race) =>
        val result = Map("fastestLaps" -> race.drivers.map(driverService.fastestLap))
        Ok(Serialization.write(result)).as(JSON)
      case None =>
        NotFound(Json.obj("msg" -> s"Race id '$id' not found"))
    }
  }

  def averageSpeedByDriver(id: String) = Action {
    raceService.find(id) match {
      case Some(race) =>
        val result = race.drivers.map(d =>
          Map("driverId" -> d.id, "driverName" -> d.name, "averageSpeed" -> driverService.avgSpeed(d)))
        Ok(Serialization.write(Map("averageSpeed" -> result))).as(JSON)
      case None =>
        NotFound(Json.obj("msg" -> s"Race id '$id' not found"))
    }
  }

}
