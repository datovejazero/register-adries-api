package eu.ideata.datahub.route


import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.typesafe.slick.driver.ms.SQLServerDriver.api._
import eu.ideata.datahub.db.Executor
import eu.ideata.datahub.domain._
import eu.ideata.datahub.marshaller.DatahubJsonMarshaller
import eu.ideata.datahub.util.DBConfigHelper
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

import scala.concurrent.duration._

/**
  * Created by mbarak on 07/11/2016.
  */
class DomainRouteIT extends WordSpec with Matchers with ScalatestRouteTest with BeforeAndAfter with DatahubJsonMarshaller {

  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(80.second)
  var db: Database = _

  var m: MunicipalityRoute = _

  "Domain object routes" should {
    "return all Municipalities" in {
      Get("/municipality") ~> m.route ~> check {
        response.status.isSuccess() shouldBe true
        val res = responseAs[List[Municipality]]
        res should not be empty
      }
    }
    "return municipality name for a given id" in {
      Get("/municipality/DA0139438D8362817802FF9D5C2EBB7C") ~> m.route ~> check {
        response.status.isSuccess() shouldBe true
      }
    }
    "return streets per municipality" in {
      Get("/municipality/DA0139438D8362817802FF9D5C2EBB7C/street") ~> m.route ~> check {
        response.status.isSuccess() shouldBe true
        val res = responseAs[List[StreetName]]
        res should not be empty
        res.map(_.streetName).toSet should have size(res.size)
        println(res)

      }
    }

    "return building ids at street name " in {
      Get("/municipality/DA0139438D8362817802FF9D5C2EBB7C/street/D225C2491E53E407C4B1F73309B22440") ~> m.route ~> check {
        response.status.isSuccess() shouldBe true
        responseAs[List[Int]] should not be empty
      }
    }
  }

  before {
    db = Database.forConfig("db", DBConfigHelper.testConfig)
    val ec = new Executor(db)
    m = new MunicipalityRoute(ec)
  }
}
