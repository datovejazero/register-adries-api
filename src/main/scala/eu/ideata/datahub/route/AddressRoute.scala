package eu.ideata.datahub.route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import eu.ideata.datahub.db.Executor
import eu.ideata.datahub.domain.{Address, Addresses}
import eu.ideata.datahub.marshaller.DatahubJsonMarshaller
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

/**
  * Created by mbarak on 12/10/16.
  */
class AddressRoute(databaseExecutor: => Executor)(implicit ec: ExecutionContext) extends DatahubJsonMarshaller {


  val route = {
    path("addresses") {
      withRequestTimeout(Duration(60, "second")) {
        get {
          respondWithHeader(RawHeader("Cache-Control", "max-age=86400")) {
            complete {
              databaseExecutor.run(Addresses.all)
            }
          }
        }
      }
    } ~
      rejectEmptyResponse {
        path("address") {
          get {
            parameters('region, 'country, 'municipality, 'streetName, 'buildingNumber.as[Int]) { (count, mun, cit, stn, bn) => {
              val address = Address(count, mun, cit, stn, bn)
              complete {
                  databaseExecutor.run(Addresses.exists(address)).map(_.headOption)
                }
              }
            }
          }
        }
      }
    }
  }
