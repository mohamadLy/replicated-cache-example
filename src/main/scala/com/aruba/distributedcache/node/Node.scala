package com.aruba.distributedcache.node

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.FromConfig
import com.aruba.distributedcache.api.Employee
import com.aruba.distributedcache.cluster.ClusterManager
import com.aruba.distributedcache.cluster.ClusterManager.GetMembers
import com.aruba.distributedcache.processor.Processor
import com.aruba.distributedcache.processor.Processor.{GetEmployeeFromCache, InsertInCache}

object Node {

  sealed trait NodeMessage

  case class InsertEmployee(employee: Employee)
  case class GetEmployee(id: Int)

  case object GetClusterMembers

  def props(nodeId: String) = Props(new Node(nodeId))
}

class Node(nodeId: String) extends Actor {
  import Node._

  val processor: ActorRef = context.actorOf(Processor.props(nodeId), "processor")
  val processorRouter: ActorRef = context.actorOf(FromConfig.props(Props.empty), "processorRouter")
  val clusterManager: ActorRef = context.actorOf(ClusterManager.props(nodeId), "clusterManager")

  override def receive: Receive = {
    case GetClusterMembers => clusterManager forward GetMembers
    case InsertEmployee(value) => processorRouter forward InsertInCache(value)
    case GetEmployee(id) => processorRouter forward GetEmployeeFromCache(id)
  }
}