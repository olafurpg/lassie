package lassie.server

import akka.event.Logging
import spray.routing.directives.DebuggingDirectives
import upickle.default._
import upickle.Js

import akka.actor.ActorSystem
import lassie.Api
import spray.client.pipelining._
import spray.http._
import spray.http.HttpEntity
import spray.http.MediaTypes
import spray.routing.SimpleRoutingApp
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object SprayServer extends SimpleRoutingApp {
  implicit val system = ActorSystem("lassie")

  val logger = Logging(system, getClass)

  def main(args: Array[String]): Unit = {
    startServer("0.0.0.0", port = 8080) {
      get {
        pathSingleSlash {
          complete {
            HttpEntity(
              MediaTypes.`text/html`,
              Template.txt
            )
          }
        } ~
          getFromResourceDirectory("")
      } ~
        post {
          path("api" / Segments) { s =>
            extract(_.request.entity.asString) { e =>
              complete {
                logger.info(s"Request $e")
                AutowireServer.route[Api](ApiImpl)(
                  autowire.Core.Request(
                    s,
                    upickle.json.read(e).asInstanceOf[Js.Obj].value.toMap
                  )
                ).map(upickle.json.write)
              }
            }
          }
        }
    }
  }
}
