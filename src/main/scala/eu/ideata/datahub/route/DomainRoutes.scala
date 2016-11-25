package eu.ideata.datahub.route

import eu.ideata.datahub.db.Executor
import eu.ideata.datahub.marshaller.DatahubJsonMarshaller
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import eu.ideata.datahub.domain.{Addresses, DomainTables}
import com.typesafe.slick.driver.ms.SQLServerDriver.api._

import scala.concurrent.ExecutionContext

/**
  * Created by mbarak on 07/11/2016.
  */
class MunicipalityRoute(db: => Executor)(implicit ec: ExecutionContext) extends DatahubJsonMarshaller {

  var route = {
    path("municipality") {
      complete {
        db.run(DomainTables.municipalities.result)
      }
    } ~
      path("municipality" / Segment) { municipalityId => {
        complete {
          println(municipalityId)
          db.run {
            (
              for {
                m <- DomainTables.municipalities.filter(_.municipalityId === municipalityId).map(_.municipalityName)
              } yield (m)).result
            }
          }
        }
      } ~
      path("municipality" / Segment / "street") { municipalityId => {
        complete {
          db.run {
            (for {
              (_, s) <- DomainTables.municipalities.filter(_.municipalityId === municipalityId) join Addresses.table on (_.municipalityName === _.municipality) join DomainTables.streetNames on (_._2.streetName === _.streetName)
            } yield (s)).result.map(_.toSet)
          }
        }
      }
    }  ~
      path("municipality" / Segment / "street" / Segment) { (municipalityId, streetId) => {
        complete {
          db.run {
            (for {
              bn <- DomainTables.municipalities.filter(_.municipalityId === municipalityId) join
                Addresses.table on (_.municipalityName === _.municipality) join
                DomainTables.streetNames.filter(_.streetNameId === streetId) on (_._2.streetName === _.streetName) map { case ((m, a), s) => a.buildingNumber }
            } yield (bn)).result
          }
        }
      }
    }
  }
}

