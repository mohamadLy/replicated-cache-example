package com.aruba.distributedcache.processor

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.aruba.distributedcache.api.Employee
import spray.json.DefaultJsonProtocol

case class ProcessorResponse(nodeId: String, result: BigInt)
case class EmployeeResponse(nodeId: String, employee: Employee)

object ProcessorResponseJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol{
  implicit val processorResponse = jsonFormat2(ProcessorResponse)
  implicit val employeeFormat = jsonFormat3(Employee)
  implicit val employeeResponse = jsonFormat2(EmployeeResponse)
}