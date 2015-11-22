package lassie

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import scala.concurrent.Future
import scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scalatags.JsDom.all._
import upickle.default._
import upickle.Js
import autowire._

object Client extends autowire.Client[Js.Value, Reader, Writer]{
  override def doCall(req: Request): Future[Js.Value] = {
    dom.ext.Ajax.post(
      url = "/api/" + req.path.mkString("/"),
      data = upickle.json.write(Js.Obj(req.args.toSeq:_*))
    ).map(_.responseText)
      .map(upickle.json.read)
  }

  def read[Result: Reader](p: Js.Value) = readJs[Result](p)
  def write[Result: Writer](r: Result) = writeJs(r)
}

case class Bounce(ms: Int)(f: () => Any) {

  var handle: Option[Int] = None

  def run: Any => Unit = { a =>
    handle.foreach(dom.clearTimeout)
    handle = Some(dom.setTimeout(f, ms))
  }
}


@JSExport
object ScalaJSExample {
  @JSExport
  def main(): Unit = {
    val inputBox = input.render
    val outputBox = div.render
    val bouncer = Bounce(1000) { () =>
      Client[Api]
        .queryMaven(inputBox.value)
        .call().map { response =>
        outputBox.innerHTML = ""
        outputBox.appendChild(
          ul(
            for(doc <- response) yield {
              li(doc.dep.moduleId + " - ", a(href := doc.dep.pomUrl, "pom"))
            }
          ).render
        )
      }
    }

    inputBox.onkeyup = bouncer.run

    dom.document.body.appendChild(
      div(
        cls:="container",
        h1("Lassie search"),
        p("Search for a Maven package."),
        inputBox,
        outputBox
      ).render
    )
  }
}
