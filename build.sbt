import com.lihaoyi.workbench.Plugin._
import spray.revolver.AppProcess
import spray.revolver.RevolverPlugin.Revolver

name := "lassie"

organization := "com.geirsson"

//lazy val root = project.in(file("."))
//  .aggregate(lassieJS, lassieJVM)

lazy val lassie = crossProject
  .settings(
    name := "lassie",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.11.7",
    testFrameworks += new TestFramework("utest.runner.Framework"),
    libraryDependencies ++= Seq(
      "org.scalatest" %%% "scalatest" % "3.0.0-SNAP5",
      "com.lihaoyi" %%% "utest" % "0.3.0" % "test",
      "com.lihaoyi" %%% "upickle" % "0.3.6",
      "com.lihaoyi" %%% "autowire" % "0.2.4",
      "com.lihaoyi" %%% "scalatags" % "0.5.2"
    )
  ).jsSettings(
    workbenchSettings:_*
  ).jvmSettings(
    Revolver.settings:_*
  ).jvmSettings(
    name := "Server",
    libraryDependencies ++= Seq(
      "com.propensive" %% "rapture-json-spray" % "2.0.0-M1",
      "com.propensive" %% "rapture-json" % "2.0.0-M1",
      "io.spray" %% "spray-json" % "1.3.1",
      "io.spray" %% "spray-client" % "1.3.1",
      "io.spray" %% "spray-can" % "1.3.1",
      "io.spray" %% "spray-routing" % "1.3.1",
      "com.typesafe.akka" %% "akka-actor" % "2.3.2",
      "org.webjars" % "bootstrap" % "3.2.0"
    )
  ).jsSettings(
    name := "web-client",
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.1"
    ),
    bootSnippet := "lassie.ScalaJSExample().main();"
  )

lazy val lassieJVM = lassie.jvm.settings(
  (resources in Compile) += {
    (fastOptJS in (lassieJS, Compile)).value
    (artifactPath in (lassieJS, Compile, fastOptJS)).value
  }
)
lazy val lassieJS = lassie.js.settings(
  refreshBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)
)

