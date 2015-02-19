package controllers

import models.JsonFormats._
import models.Vessel
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import repositories.{VesselsPersistence, VesselsMongodb}

import scala.concurrent.Future

object Vessels extends Controller {

  val persistence: VesselsPersistence = VesselsMongodb

  def list = Action.async {
    val vesselListFuture: Future[List[Vessel]] = persistence.list()
    vesselListFuture.map { vessels =>
      Ok(Json.toJson(vessels))
    }
  }

  def get(id: String) = Action.async {
    val future: Future[Option[Vessel]] = persistence.get(id)
    future.map {
      case v: Some[Vessel] => Ok(Json.toJson(v))
      case None => NotFound(Json.obj("error" -> s"Vessel '$id' not found"))
    }
  }

  def create = Action.async(parse.json) { request =>
    request.body.validate[Vessel].map{ vessel =>
      val future: Future[Unit] = persistence.create(vessel)
      future.map { _ =>
        Created(Json.obj("message" -> "Vessel created successfully"))
      }
    }.getOrElse(Future.successful(BadRequest(Json.obj("error" -> "Post data has no valid Vessel format"))))
  }

  def update(id: String) = Action.async(parse.json) { request =>
    val future: Future[Option[Vessel]] = persistence.get(id)
    future.flatMap {
      case None => Future.successful(NotFound(Json.obj("error" -> s"Vessel '$id' not found")))
      case v: Some[Vessel] =>
        request.body.validate[Vessel].map{ vesselRequest =>
          val vesselToUpdate = vesselRequest.copy(id = id)
          val future: Future[Unit] = persistence.update(vesselToUpdate)
          future.map { _ =>
            Ok(Json.obj("message" -> "Vessel updated successfully"))
          }
        }.getOrElse(Future.successful(BadRequest(Json.obj("error" -> "Post data has no valid Vessel format"))))
    }
  }

  def delete(id: String) = Action.async {
    val future: Future[Option[Vessel]] = persistence.get(id)
    future.flatMap {
      case None => Future.successful(NotFound(Json.obj("error" -> s"Vessel '$id' not found")))
      case v: Some[Vessel] =>
        val future: Future[Unit] = persistence.delete(id)
        future.map { _ =>
          Ok(Json.obj("message" -> "Vessel deleted successfully"))
        }
    }
  }

}