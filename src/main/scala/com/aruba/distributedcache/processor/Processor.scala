package com.aruba.distributedcache.processor

import akka.actor.{Actor, ActorRef, Props}
import com.aruba.distributedcache.api.Employee
import com.aruba.distributedcache.processor.CacheProcessor.{Get, Put}

object Processor {

  sealed trait ProcessorMessage

  case class InsertInCache(employee: Employee) extends ProcessorMessage
  case class GetEmployeeFromCache(id: Int) extends ProcessorMessage

  def props(nodeId: String) = Props(new Processor(nodeId))
}

class Processor(nodeId: String) extends Actor {

  import Processor._

  val cacheProcessor: ActorRef = context.actorOf(CacheProcessor.props(nodeId), "replicated-cache")

  override def receive: Receive = {
    case InsertInCache(employee) =>
      val replyTo = sender()
      cacheProcessor ! Put(employee, replyTo)

    case GetEmployeeFromCache(id) =>
      val replyTo = sender()
      cacheProcessor ! Get(id, replyTo)

  }
}
