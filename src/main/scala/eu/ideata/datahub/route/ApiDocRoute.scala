package eu.ideata.datahub.route

import akka.http.scaladsl.server.Directives._

/**
  * Created by mbarak on 13/10/2016.
  */
class ApiDocRoute {
  val route = {
    path("documentation"){
      encodeResponse {
        getFromResource("swagger/swagger.json")
      }
    }
  }
}
