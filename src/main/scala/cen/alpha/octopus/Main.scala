package cen.alpha.octopus

import akka.actor.ActorSystem
import cen.alpha.octopus.api.OctopusService

object Main extends App {

  println("Starting octopus main...")

  val system = ActorSystem("octopus-system")

  val server = system.actorOf(OctopusService.props())

  // ...

}
