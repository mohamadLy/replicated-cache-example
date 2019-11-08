package com.aruba.distributedcache.processor

import akka.actor.{Actor, ActorRef, Props}
import com.aruba.distributedcache.api.Employee

import scala.annotation.tailrec

object ProcessorFibonacci {
  sealed trait ProcessorFibonacciMessage
  case class Compute(n: Int, replyTo: ActorRef) extends ProcessorFibonacciMessage
  case class Put(employee: Employee, replyTo: ActorRef) extends ProcessorFibonacciMessage

  def props(nodeId: String) = Props(new ProcessorFibonacci(nodeId))

  def fibonacci(x: Int): BigInt = {
    @tailrec def fibHelper(x: Int, prev: BigInt = 0, next: BigInt = 1): BigInt = x match {
      case 0 => prev
      case 1 => next
      case _ => fibHelper(x - 1, next, next + prev)
    }
    fibHelper(x)
  }
}

class ProcessorFibonacci(nodeId: String) extends Actor {
  import ProcessorFibonacci._

  override def receive: Receive = {
    case Compute(value, replyTo) => {
      replyTo ! ProcessorResponse(nodeId, fibonacci(value))
    }
    case Put(employee, replyTo) =>
      replyTo ! EmployeeResponse(nodeId, employee)

  }
}
