package lassie.server

object Template {
  import scalatags.Text.all._
  import scalatags.Text.tags2.title
  val txt =
    "<!DOCTYPE html>" +
      html(
        head(
          title("Example Scala.js application"),
          meta(httpEquiv := "Content-Type", content := "text/html; charset=UTF-8"),
          script(`type` := "text/javascript", src := "/web-client-fastopt.js"),
          script(`type` := "text/javascript", src := "//localhost:12345/workbench.js"),
          link(
            rel := "stylesheet",
            `type` := "text/css",
            href := "META-INF/resources/webjars/bootstrap/3.2.0/css/bootstrap.min.css"
          )
        ),
        body(margin := 0)(
          script("lassie.ScalaJSExample().main()")
        )
      )
}
