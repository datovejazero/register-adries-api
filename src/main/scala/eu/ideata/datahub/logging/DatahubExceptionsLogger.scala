package eu.ideata.datahub.logging

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by mbarak on 12/10/16.
  */
trait DatahubExceptionsLogger extends LazyLogging {
  implicit def logsfinderExceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: Exception =>
      extractUri { uri =>
        logger.error(s"An wild exception on $uri has occure", e)
        complete {
          HttpResponse(StatusCodes.InternalServerError, entity =  HttpEntity("Internal server error"))
        }
      }
  }
}
