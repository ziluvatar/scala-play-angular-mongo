package repositories

import models.JsonFormats._
import models.Vessel
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.json.BSONFormats._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.{BSONObjectID, BSONDocument}

import scala.concurrent.Future

object VesselsMongodb extends VesselsPersistence with PlayReactiveMongodb {

  def collection: JSONCollection = db.collection[JSONCollection]("vessels")

  collection.indexesManager.ensure(
    Index(List("id" -> IndexType.Text), unique = true)
  )

  override def list(): Future[List[Vessel]] = {
    val cursor: Cursor[Vessel] = collection.find(BSONDocument()).cursor[Vessel]
    cursor.collect[List]()
  }

  override def create(entity: Vessel): Future[Unit] = {
    collection.insert(entity.copy(id = BSONObjectID.generate.stringify)).map{ _ => Unit }
  }

  override def update(entity: Vessel): Future[Unit] = {
    collection.update(queryId(entity.id), entity).map{ _ => Unit }
  }

  override def get(id: String): Future[Option[Vessel]] = {
    val cursor: Cursor[Vessel] = collection.find(queryId(id)).cursor[Vessel]
    cursor.headOption
  }

  override def delete(id: String): Future[Unit] = {
    collection.remove(queryId(id)).map{ _ => Unit }
  }

  private def queryId(id: String): JsObject = {
    Json.obj("id" -> id)
  }
}
