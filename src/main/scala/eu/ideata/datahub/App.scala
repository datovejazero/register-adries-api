package eu.ideata.datahub

import java.io.File

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.CorsDirectives.cors
import ch.megard.akka.http.cors.CorsSettings
import com.typesafe.config.ConfigFactory
import eu.ideata.datahub.config.DatahubConfigParser
import eu.ideata.datahub.db.Executor
import eu.ideata.datahub.logging.DatahubExceptionsLogger
import eu.ideata.datahub.route._

/**
  * Created by mbarak on 12/10/16.
  */

object App extends DatahubExceptionsLogger {
  implicit val system = ActorSystem("datahub-web-api")
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]) {
    import system.dispatcher

    val appConfig = DatahubConfigParser.parse(args)

    val config = ConfigFactory.parseFile(new File(appConfig.configPath))

    lazy val (db, executor) = Executor.createDBAndExecutor(config)

    val webConfig = config.getConfig("app")

    val host = webConfig.getString("host")
    val port = webConfig.getInt("port")
    val apiVersion = webConfig.getString("api-version")

    val addressesRoute = new AddressRoute(executor)
    val municipalityRoute = new MunicipalityRoute(executor)
    val apiDocRoute = new ApiDocRoute

    val corsSettings = CorsSettings.defaultSettings.copy()

    val routes = cors(corsSettings) {
      withPrefixAndVersion(apiVersion) {
        addressesRoute.route ~
        apiDocRoute.route ~
        municipalityRoute.route
      }
    }

    val bindings = Http().bindAndHandle(routes, host, port)
      .map(_ => logger.info("Server started"))
      .recover {
        case e: Exception => {
          logger.error("Failed to start due: ", e)
        }
      }
  }

  def withPrefixAndVersion(version: String)(routes: Route) = pathPrefix("api"){
    pathPrefix(version) {
      logRequest("requested: ", Logging.InfoLevel) {
        routes
      }
    }
  }

}
