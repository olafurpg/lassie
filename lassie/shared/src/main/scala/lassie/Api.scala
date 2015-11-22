package lassie

import derive.key

import scala.concurrent.Future

trait Api {
  def queryMaven(q: String): Future[List[Doc]]
}

case class Param(
                  spellcheck: String,
                  fl: String,
                  sort: String,
                  indent: String,
                  q: String,
                  qf: String,
                  @key("spellcheck.count")
                  spellcheckCount: String,
                  wt: String,
                  rows: String,
                  version: String,
                  defType: String)
case class ResponseHeader(
                           status: Int,
                           QTime: Int,
                           params: Param)
case class Response(
                     numFound: Int,
                     start: Int,
                     docs: List[Doc])
case class Doc(id: String,
               g: String,
               a: String,
               latestVersion: String,
               repositoryId: String,
               p: String,
               timestamp: Long,
               versionCount: Int,
               text: List[String],
               ec: List[String]) {
  def dep: Dep = Dep(g, a, latestVersion)
}

case class SpellSuggestion(
                            numFound: Int,
                            startOffset: Int,
                            endOffset: Int,
                            suggestion: Option[String]
                          )

case class MavenResponse(
                          responseHeader: ResponseHeader,
                          response: Response,
                          spellcheck: SpellSuggestion
                        )

case class Dep(org: String, name: String, version: String, scope: Option[String] = None) {
  def moduleId: String = s"$org % $name % $version"
  def path = org.split("\\.", -1).mkString("/")
  def pomUrl =
    s"https://repo1.maven.org/maven2/$path/$name/$version/$name-$version.pom"
}
