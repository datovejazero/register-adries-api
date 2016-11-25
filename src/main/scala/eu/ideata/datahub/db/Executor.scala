package eu.ideata.datahub.db
import com.typesafe.config.Config
import com.typesafe.slick.driver.ms.SQLServerDriver.api._
import slick.dbio.{DBIOAction, NoStream}

import scala.concurrent.Future
/**
  * Created by mbarak on 12/10/16.
  */
class Executor(db: Database) {
  def run[T](a: DBIOAction[T, NoStream, Nothing]): Future[T] = db.run[T](a)
  def close = db.close()
}


object Executor {
  def createDBAndExecutor(conf: Config): (Database, Executor) = {
    val db = Database.forConfig("db", conf)
    (db, new Executor(db))
  }
}