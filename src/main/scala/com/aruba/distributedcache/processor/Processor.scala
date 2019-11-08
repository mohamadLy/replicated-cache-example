package com.aruba.distributedcache.processor

import akka.actor.{Actor, ActorRef, Props}
import com.aruba.distributedcache.api.Employee
import com.aruba.distributedcache.processor.ProcessorFibonacci.{Compute, Put}

object Processor {

  sealed trait ProcessorMessage

  case class ComputeFibonacci(n: Int) extends ProcessorMessage
  case class InsertInCache(employee: Employee) extends ProcessorMessage

  def props(nodeId: String) = Props(new Processor(nodeId))
}

class Processor(nodeId: String) extends Actor {

  import Processor._

  val fibonacciProcessor: ActorRef = context.actorOf(ProcessorFibonacci.props(nodeId), "fibonacci")

  override def receive: Receive = {
    case ComputeFibonacci(value) => {
      val replyTo = sender()
      fibonacciProcessor ! Compute(value, replyTo)
    }
    case InsertInCache(employee) =>
      val replyTo = sender()
      fibonacciProcessor ! Put(employee, replyTo)

  }
}
