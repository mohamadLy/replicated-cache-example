package com.aruba.distributedcache.processor

import akka.actor.{Actor, ActorRef, Props}
import com.aruba.distributedcache.api.Employee
import com.typesafe.scalalogging.LazyLogging
import com.hazelcast.core.{EntryEvent, EntryListener, Hazelcast, MapEvent, ReplicatedMap}
import java.util.concurrent.TimeUnit

object CacheProcessor extends LazyLogging {
  sealed trait ProcessorCacheMessage
  case class Put(employee: Employee, replyTo: ActorRef) extends ProcessorCacheMessage
  case class Get(id: Int, replyTo: ActorRef) extends ProcessorCacheMessage

  def props(nodeId: String) = Props(new CacheProcessor(nodeId))

  val hazelcastInstance = Hazelcast.newHazelcastInstance()

  val employees: ReplicatedMap[String, Employee] = hazelcastInstance.getReplicatedMap("employees")

  employees.addEntryListener(new EntryListener[String, Employee]() {
    override def entryAdded(event: EntryEvent[String, Employee]): Unit = {
      logger.info("Entry added: " + event)
    }
    override def entryRemoved(event: EntryEvent[String, Employee]): Unit = {
      logger.info("Entry Removed: " + event)
    }
    override def entryUpdated(event: EntryEvent[String, Employee]): Unit = {
      logger.info("Entry updated: " + event)
    }
    override def entryEvicted(event: EntryEvent[String, Employee]): Unit = {
      logger.info("Entry evicted: " + event)
    }

    override def mapEvicted(event: MapEvent): Unit = ???
    override def mapCleared(event: MapEvent): Unit = ???
  })

}

class CacheProcessor(nodeId: String) extends Actor with LazyLogging {
  import CacheProcessor._

  override def preStart(): Unit = {
    employees.put("1", Employee("Joe", "Doe", "1"), 2, TimeUnit.MINUTES)
    employees.put("2", Employee("Luke", "White", "2"), 2, TimeUnit.MINUTES)
    employees.put("3", Employee("Larissa", "Brown", "3"), 2, TimeUnit.MINUTES)
  }

  override def receive: Receive = {
    case Put(employee, replyTo) =>
      logger.info("Inserting new Employee= {}, replyTo={}", employee, replyTo)
      employees.put(employee.id, employee, 2, TimeUnit.MINUTES)
      replyTo ! EmployeeResponse(nodeId, employee)

    case Get(id, replyTo) =>
      logger.info("Before querying the cache id={}, replyTo={}", id, replyTo)
      val currentEmployee = employees.get(id.toString)
      logger.info("Getting employee {}", currentEmployee)
      replyTo ! EmployeeResponse(nodeId, currentEmployee)

  }
}
