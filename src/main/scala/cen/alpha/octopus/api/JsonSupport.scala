package cen.alpha.octopus.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import cen.alpha.octopus.Hello
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val helloFormat = jsonFormat1(Hello)

}
