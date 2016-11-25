package eu.ideata.datahub.marshaller

import eu.ideata.datahub.domain._
import spray.json.DefaultJsonProtocol

/**
  * Created by mbarak on 12/10/16.
  */
trait DatahubJsonMarshaller extends DefaultJsonProtocol {
  implicit val addressProtocol = jsonFormat5(Address.apply)
  implicit val municipalityProtocol = jsonFormat2(Municipality.apply)
  implicit val streetNameProtocol = jsonFormat2(StreetName.apply)
}
