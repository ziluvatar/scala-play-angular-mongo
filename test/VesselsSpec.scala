import java.util.concurrent.TimeUnit

import com.github.athieriot.{CleanAfterExample, EmbedConnection}
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import models.JsonFormats._
import models.{Location, Vessel, VesselDimensions}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification.BeforeExample
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration

@RunWith(classOf[JUnitRunner])
class VesselsSpec extends Specification with EmbedConnection with CleanAfterExample with BeforeExample {
  sequential

  val timeout = FiniteDuration(5, TimeUnit.SECONDS)

  lazy val coll = mongoClient.getDB("vessels-mng-test").getCollection("vessels")
  lazy val application = FakeApplication()


  step(play.api.Play.start(application))
  def before {
    coll.insert(
      createDBVessel("vessel-1"),
      createDBVessel("vessel-2"),
      createDBVessel("vessel-3"),
      createDBVessel("vessel-4"))
  }

  override def after() {
    super.after()
    coll.dropIndex("id_text")
  }

  "Vessels" should {

    "list all vessels" in {
      val response = route(FakeRequest(GET, "/vessels")).get
      Await.result(response, timeout)

      status(response) mustEqual OK

      val jsonObject = Json.parse(contentAsString(response))
      val vessels = jsonObject.asOpt[List[Vessel]].get
      vessels.size mustEqual 4
    }

    "retrieve an existent vessel" in {
      val response = route(FakeRequest(GET, "/vessels/vessel-1-id")).get
      Await.result(response, timeout)

      status(response) mustEqual OK

      val jsonObject = Json.parse(contentAsString(response))
      val vessel = jsonObject.asOpt[Vessel].get
      vessel.name mustEqual "vessel-1"
    }

    "return 404 when a vessel does not exist" in {
      val response = route(FakeRequest(GET, "/vessels/b")).get
      Await.result(response, timeout)

      status(response) mustEqual NOT_FOUND
      contentAsString(response) mustEqual """{"error":"Vessel 'b' not found"}"""
    }

    "create a new Vessel" in {
      val newVessel = new Vessel("vessel-5", VesselDimensions(8, 300, 20), Location(5, 9))
      val response = route(FakeRequest(POST, "/vessels").withJsonBody(Json.toJson(newVessel))).get
      Await.result(response, timeout)

      status(response) mustEqual CREATED

      val vesselsFound = coll.count(MongoDBObject("name" -> "vessel-5"))
      vesselsFound mustEqual 1
    }

    "return 400 when the data sent for creation is wrong" in {
      val response = route(FakeRequest(POST, "/vessels").withJsonBody(Json.obj("a" -> "b"))).get
      Await.result(response, timeout)

      status(response) mustEqual BAD_REQUEST
      contentAsString(response) mustEqual """{"error":"Post data has no valid Vessel format"}"""
    }

    "update a existent Vessel" in {
      val updatedVessel = new Vessel("modified-vessel-4", VesselDimensions(8, 300, 20), Location(5, 9), "4")
      val response = route(
        FakeRequest(PUT, "/vessels/vessel-4-id").withJsonBody(Json.toJson(updatedVessel))).get
      Await.result(response, timeout)

      status(response) mustEqual OK

      val vesselsFound = coll.count(MongoDBObject("name" -> "modified-vessel-4"))
      vesselsFound mustEqual 1
    }

    "update a existent Vessel with wrong data" in {
      val response = route(FakeRequest(PUT, "/vessels/vessel-4-id").withJsonBody(Json.obj("a" -> "b"))).get
      Await.result(response, timeout)

      status(response) mustEqual BAD_REQUEST
      contentAsString(response) mustEqual """{"error":"Post data has no valid Vessel format"}"""
    }

    "update a non existent Vessel" in {
      val updatedVessel = new Vessel("modified-vessel-4", VesselDimensions(8, 300, 20), Location(5, 9), "4")
      val response = route(FakeRequest(PUT, "/vessels/b").withJsonBody(Json.toJson(updatedVessel))).get
      Await.result(response, timeout)

      status(response) mustEqual NOT_FOUND
      contentAsString(response) mustEqual """{"error":"Vessel 'b' not found"}"""
    }

    "delete a existent Vessel" in {
      val response = route(FakeRequest(DELETE, "/vessels/vessel-2-id")).get
      Await.result(response, timeout)

      status(response) mustEqual OK

      val vesselsFound = coll.count(MongoDBObject("id" -> "vessels-2-id"))
      vesselsFound mustEqual 0
    }

    "delete a non existent Vessel" in {
      val response = route(FakeRequest(DELETE, "/vessels/b")).get
      Await.result(response, timeout)

      status(response) mustEqual NOT_FOUND
      contentAsString(response) mustEqual """{"error":"Vessel 'b' not found"}"""
    }

  }

  step(play.api.Play.stop())

  def createDBVessel(name: String): DBObject = {
    com.mongodb.util.JSON.parse(s"""{ "name" : "$name", "dimensions" : { "width" : 8, "length" : 300, "draft" : 20 }, "lastLocation" : { "latitude" : 5, "longitude" : 9 }, "id" : "$name-id" }""").asInstanceOf[DBObject]
  }

}
