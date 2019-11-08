package com.aruba.distributedcache.cache
/*
import java.util.concurrent.TimeUnit

import akka.actor.Actor
import com.aruba.distributedcache.api.Employee
import com.aruba.distributedcache.processor.CacheProcessor.Get
import com.hazelcast.core.{Hazelcast, IMap, ReplicatedMap}
import com.typesafe.scalalogging.LazyLogging

object ReplicatedCache {

  val hazelcastInstance = Hazelcast.newHazelcastInstance()

  val employees: ReplicoatedMap[String, Employee] = hazelcastInstance.getReplicatedMap("employees")

  case class GetEmployee(id: Int)

}

class ReplicatedCache extends Actor with LazyLogging {
  import ReplicatedCache._

  override def preStart(): Unit = {
    employees.put("1", Employee("Joe", "Doe", "1"), 2, TimeUnit.MINUTES)
    employees.put("2", Employee("Luke", "White", "2"), 2, TimeUnit.MINUTES)
    employees.put("3", Employee("Larissa", "Brown", "3"), 2, TimeUnit.MINUTES)
  }
  override def receive: Receive = {
//    case Put(employee: Employee) =>

    case GetEmployee(id) =>
      sender() ! employees.get(id)

  }
}
 */
