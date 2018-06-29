package cen.alpha.octopus.server

import akka.actor.ActorRefFactory
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext

trait Service {

  def actorRefFactory: ActorRefFactory

  implicit val executionContext: ExecutionContext = actorRefFactory.dispatcher

  def route: Route

}
