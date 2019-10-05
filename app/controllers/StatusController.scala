package controllers

import javax.inject._
import org.hiroinamine.BuildInfo
import play.api.mvc._

@Singleton
class StatusController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def get = Action {
    Ok(BuildInfo.toJson).as(JSON)
  }
}
