package eu.ideata.datahub.domain

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}
import com.typesafe.slick.driver.ms.SQLServerDriver.api._
import eu.ideata.datahub.db.Executor
import eu.ideata.datahub.util.DBConfigHelper
import org.scalatest.time.{Millis, Seconds, Span}
/**
  * Created by mbarak on 12/10/16.
  */
class AddressFiltrationIT extends WordSpec with Matchers with ScalaFutures with BeforeAndAfter {

  implicit val defaultPatience =
    PatienceConfig(timeout =  Span(30, Seconds), interval = Span(100, Millis))

  var db: Database = _
  var executor: Executor = _

  val existingAdress = Address("Bratislava", "Bratislava", "Bratislava", "Jancova", 15)
  val nonExisitngAdress = Address("Neexistujuce", "Neexistujuce", "Neexistujuce", "Ulica", 13)

  "Addresses" should {

    "Return an address if it exists" in {
      whenReady(executor.run(Addresses.exists(existingAdress))) {
        result =>
          result shouldBe Vector(existingAdress)
      }

      whenReady(executor.run(Addresses.exists(nonExisitngAdress))) {
        result =>
          result shouldBe empty
      }
    }

    "Return all addresses" in {
      whenReady(executor.run(Addresses.all)){
        result =>
          result should have size(2)
      }
    }
  }



  before {
    db = Database.forConfig("db", DBConfigHelper.testConfig)
    executor = new Executor(db)
  }

  after {
    db.close()
  }
}
