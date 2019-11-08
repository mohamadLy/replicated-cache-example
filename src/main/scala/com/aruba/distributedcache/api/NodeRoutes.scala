package com.aruba.distributedcache.api

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask

import scala.concurrent.duration._
import com.aruba.distributedcache.node.Node.{GetClusterMembers, GetEmployee, InsertEmployee}
import com.aruba.distributedcache.processor.{EmployeeResponse}
import com.typesafe.scalalogging.LazyLogging
import com.aruba.distributedcache.processor.ProcessorResponseJsonProtocol._

import scala.concurrent.Future

trait NodeRoutes extends SprayJsonSupport with LazyLogging {

  implicit def system: ActorSystem

  def node: ActorRef

  implicit lazy val timeout = Timeout(5.seconds)

  lazy val healthRoute: Route = pathPrefix("health") {
    concat(
      pathEnd {
        concat(
          get {
            complete(StatusCodes.OK)
          }
        )
      }
    )
  }

  lazy val statusRoutes: Route = pathPrefix("status") {
    concat(
      pathPrefix("members") {
        concat(
          pathEnd {
            concat(
              get {
                val membersFuture: Future[List[String]] = (node ? GetClusterMembers).mapTo[List[String]]
                onSuccess(membersFuture) { members =>
                  complete(StatusCodes.OK, members)
                }
              }
            )
          }
        )
      }
    )
  }

  lazy val insertEmployee: Route = pathPrefix("insert") {
    concat(
      pathPrefix("employee") {
        concat(
          pathEnd {
            concat(
              post {
                entity(as[Employee]) { employee =>
                  logger.info("employee: " + employee)
                  val processFuture: Future[EmployeeResponse] = (node ? InsertEmployee(employee)).mapTo[EmployeeResponse]
                  onSuccess(processFuture) { response =>
                    complete(StatusCodes.OK, response)
                  }
                }
              }
            )
          }
        )
      }
    )
  }

  lazy val getEmployee: Route = pathPrefix("employee") {
    concat(
      path(IntNumber) { n =>
        pathEnd {
          concat(
            get {
              val employeeFuture: Future[EmployeeResponse] = (node ? GetEmployee(n)).mapTo[EmployeeResponse]
              onSuccess(employeeFuture) { response =>
                complete(StatusCodes.OK, response)
              }
            }
          )
        }
      }
    )
  }
}
