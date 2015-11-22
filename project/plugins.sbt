resolvers ++= Seq(
  "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
  , "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  , "Flyway" at "http://flywaydb.org/repo"
  , "Spray repo" at "http://repo.spray.io"
  , "Atlassian Releases" at "https://maven.atlassian.com/public/"
)

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.4")

addSbtPlugin("com.lihaoyi" % "workbench" % "0.2.3")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.1")
