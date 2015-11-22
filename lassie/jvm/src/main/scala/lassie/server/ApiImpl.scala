package lassie.server

import akka.actor.ActorSystem
import lassie.Api
import lassie.Doc
import rapture.json.Json
import spray.http.HttpRequest
import spray.http.HttpResponse

import rapture._
import rapture.json._

import spray.client.pipelining._
import spray.http._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ApiImpl extends Api {
  implicit val actor = ActorSystem("maven")

  def pipeline: HttpRequest => Future[HttpResponse] = sendReceive

  override def queryMaven(userQuery: String): Future[List[Doc]] = {
    val query = if (userQuery.length < 3) "slick" else userQuery
    val url = s"http://search.maven.org/solrsearch/select?q=$query&rows=20&wt=json"

    import jsonBackends.spray._
    val result = pipeline(Get(url)).map {
      response =>
        Json.parse(response.entity.asString).response.docs.as[List[Doc]]
    }
    result
  }

}
