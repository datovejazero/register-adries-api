package eu.ideata.datahub.route

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import eu.ideata.datahub.util.DBConfigHelper
import com.typesafe.slick.driver.ms.SQLServerDriver.api._
import eu.ideata.datahub.db.Executor
import eu.ideata.datahub.marshaller.DatahubJsonMarshaller
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{FormData, StatusCodes}
import akka.http.scaladsl.server.Route
import eu.ideata.datahub.domain.Address
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

import scala.concurrent.duration.DurationInt

/**
  * Created by mbarak on 12/10/16.
  */
class AddressRouteIT extends WordSpec with Matchers with ScalatestRouteTest with BeforeAndAfter with DatahubJsonMarshaller {
  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(5.second)

  var db: Database = _
  var r: AddressRoute = _

  "AddressRoute" should {
    "Return all the addresses" in {
      Get("/addresses") ~> r.route ~> check {
        response.status.isSuccess() shouldBe true
        val result = responseAs[Iterable[Address]]
        result should not be empty
        result should have size(2)
      }
    }

    "Return an address if it is valid" in {
      val existingAdress = Address("Bratislava", "Bratislava", "Bratislava", "Jancova", 15)
      val nonExisitngAdress = Address("Debilne", "Debilne", "Debilne", "Ulica", 13)

      Get("/address?region=Bratislava&country=Bratislava&municipality=Bratislava&streetName=Jancova&buildingNumber=15") ~> r.route ~> check {
        response.status.isSuccess() shouldBe true
        val res = responseAs[Address] should be equals(existingAdress)
      }

      Get("/address?region=Debilne&country=Debilne&municipality=Debilne&streetName=Ulica&buildingNumber=13") ~> Route.seal(r.route) ~> check {
        status shouldEqual StatusCodes.NotFound
      }
    }
  }

  before {
    db = Database.forConfig("db", DBConfigHelper.testConfig)
    r = new AddressRoute(new Executor(db))
  }

  after {
    db.close()
  }
}
