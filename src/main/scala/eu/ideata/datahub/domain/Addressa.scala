package eu.ideata.datahub.domain
import com.typesafe.slick.driver.ms.SQLServerDriver.api._

/**
  * Created by mbarak on 11/10/16.
  */
case class Address(region: String, country: String, municipality: String, streetName: String, buildingNumber: Int)

class Addresses(tag: Tag) extends Table[Address](tag, "v_address") {

  def region = column[String]("region")
  def country = column[String]("country")
  def municipality = column[String]("municipality")
  def streetName = column[String]("street_name")
  def buildingNumber = column[Int]("building_number")

  def * = (region, country, municipality, streetName, buildingNumber) <> (Address.tupled, Address.unapply)
}

object Addresses {
  val table = TableQuery[Addresses]
  def exists(adress: Address) = table.filter(d => {
      d.region === adress.region &&
      d.country === adress.country &&
      d.municipality === adress.municipality &&
      d.streetName === adress.streetName &&
      d.buildingNumber === adress.buildingNumber
  }).result

  def all = table.result
}
