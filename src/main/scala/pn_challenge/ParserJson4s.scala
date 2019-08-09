package pn_challenge

import java.io.File
import java.util.UUID

import org.json4s._
import org.json4s.native.Serialization
import pn_challenge.utils.{CountryCode, PathConfig}

import scala.io.Source

object ParserJson4s extends App {

  implicit lazy val formats: Formats = Serialization.formats(NoTypeHints) ++ org.json4s.ext.JavaTypesSerializers.all

  case class Click(impressionId: UUID, revenue: Float)

  case class Impression(appId: Long, advertiserId: Long, countryCode: Option[CountryCode], id: UUID)

  private def getListOfFiles(dir: String): List[File] = {
    val directory: File = new File(dir)

    if (directory.exists && directory.isDirectory)
      directory.listFiles.filter(_.isFile).toList
    else List()
  }

  private def readObjects[A](path: String)(implicit m: Manifest[A]): List[A] = Extraction.extract[List[A]] {
    jackson.parseJson(
      Source.fromFile(path).mkString
    ).camelizeKeys
  }

  private def readFiles[A](files: List[File])(implicit m: Manifest[A]): List[A] =
    files.flatMap(
      file => readObjects(file.getPath)
    )

  private val clickFiles: List[File] = getListOfFiles(PathConfig.pathToClicks)
  private val impressionFiles: List[File] = getListOfFiles(PathConfig.pathToImpressions)

  val clicks: List[Click] = readFiles[Click](clickFiles)
  val impressions: List[Impression] = readFiles[Impression](impressionFiles)

}
