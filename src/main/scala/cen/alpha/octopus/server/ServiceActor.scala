package cen.alpha.octopus.server

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRefFactory, Status}
import akka.http.scaladsl.Http
import akka.pattern.pipe
import akka.stream.ActorMaterializer

object ServiceActor {

}

trait ServiceActor extends Actor with Service with ActorLogging {

  def host: String

  def port: Int

  override def actorRefFactory: ActorRefFactory = context

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  override def preStart(): Unit = {
    super.preStart()
    log.info("Starting server on {}:{}", host, port)
    Http(context.system).bindAndHandle(route, host, port).pipeTo(self)
    ()
  }

  override def receive: Receive = {

    case Http.ServerBinding(address) => handleServerBinding(address)

    case Status.Failure(cause) => handleBindFailure(cause)

  }

  def handleServerBinding(address: InetSocketAddress): Unit = {
    log.info("Listening on {}", address)
    context.become(Actor.emptyBehavior)
  }

  def handleBindFailure(cause: Throwable): Unit = {
    log.error(cause, "Can't bind to {}:{}", host, port)
    context.stop(self)
  }

}
