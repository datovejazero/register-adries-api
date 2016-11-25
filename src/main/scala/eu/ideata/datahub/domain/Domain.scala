package eu.ideata.datahub.domain
import com.typesafe.slick.driver.ms.SQLServerDriver.api._

/**
  * Created by mbarak on 07/11/2016.
  */


case class Municipality(municipalityId: String, municipalityName: String)

class Municipalities(tag: Tag) extends Table[Municipality] (tag, "v_municipality") {
  def municipalityId = column[String]("municipality_id")
  def municipalityName = column[String]("municipality_name")

  def * = (municipalityId, municipalityName) <> (Municipality.tupled, Municipality.unapply)
}

case class StreetName(streetNameId: String, streetName: String)

class StreetNames(tag: Tag) extends Table[StreetName] (tag, "v_street_name") {
  def streetNameId = column[String]("street_name_id")
  def streetName = column[String]("street_name")

  def * = (streetNameId, streetName) <> (StreetName.tupled, StreetName.unapply)
}

object DomainTables {
  val municipalities: TableQuery[Municipalities] = TableQuery[Municipalities]
  val streetNames = TableQuery[StreetNames]
}


