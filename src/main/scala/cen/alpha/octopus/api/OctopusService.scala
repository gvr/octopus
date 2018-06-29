package cen.alpha.octopus.api

import akka.actor.Props
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import cen.alpha.octopus.Hello
import cen.alpha.octopus.server.{Service, ServiceActor}
import com.typesafe.config.{Config, ConfigFactory}

object OctopusService {

  private val config: Config = ConfigFactory.load().getConfig("octopus.api")

  trait Configuration {

    lazy val host: String = config.getString("host")

    lazy val port: Int = config.getInt("port")

  }

  def props() = Props(new ServiceActor with OctopusService with Configuration)

}

trait OctopusService extends Service with JsonSupport {

  override lazy val route: Route = handleExceptions(exceptionHandler) {
    helloPath ~ dividePath
  }

  private val exceptionHandler = ExceptionHandler {

    case t: Throwable => extractUri { uri =>
      t.printStackTrace()
      println(s"The request could not be handled normally ${uri}")
      complete(HttpResponse(InternalServerError, entity = "The request could not be handled normally."))
    }

  }

  private val helloPath = path("hello") {
    get {
      complete(Hello("World!"))
    }
  }

  private val dividePath = path("divide" / IntNumber / "by" / IntNumber) { (x: Int, y: Int) =>
    get {
      val div = x / y
      val rem = x - y * div
      complete {
        if (rem == 0) s"answer: $div"
        else s"answer: $div with remainder $rem"
      }
    }
  }

}
