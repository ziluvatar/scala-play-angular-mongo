package models

case class Vessel(name: String, dimensions: VesselDimensions, lastLocation: Location, id: String = "")
case class VesselDimensions(width: Int, length: Int, draft: Int)
case class Location(latitude: Double, longitude: Double)

object JsonFormats {
  import play.api.libs.json.Json

  implicit val vesselDimensionsJsonFormat = Json.format[VesselDimensions]
  implicit val locationJsonFormat = Json.format[Location]
  implicit val vesselJsonFormat = Json.format[Vessel]
}