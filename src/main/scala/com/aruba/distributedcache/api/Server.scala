package com.aruba.distributedcache.api

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.aruba.distributedcache.node.Node
import com.typesafe.config.{Config, ConfigFactory}
import akka.http.scaladsl.server.Directives._
import scala.concurrent.Await
import scala.concurrent.duration._

object Server extends App with NodeRoutes {

  implicit val system: ActorSystem = ActorSystem("distributed-cache-demo")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config: Config = ConfigFactory.load()
  val address = config.getString("http.ip")
  val port = config.getInt("http.port")
  val nodeId = config.getString("clustering.ip")

  val node: ActorRef = system.actorOf(Node.props(nodeId), "node")

  lazy val routes: Route = healthRoute ~ statusRoutes ~ processRoutes

  Http().bindAndHandle(routes, address, port)
  println(s"Node $nodeId is listening at http://$address:$port")

  Await.result(system.whenTerminated, Duration.Inf)

}
