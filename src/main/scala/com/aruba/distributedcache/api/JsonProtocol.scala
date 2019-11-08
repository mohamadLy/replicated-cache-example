package com.aruba.distributedcache.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

class JsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val employeeFormat = jsonFormat3(Employee)
}
