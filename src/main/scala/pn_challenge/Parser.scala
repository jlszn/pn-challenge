package pn_challenge

import java.io.File
import java.util.UUID

import org.json4s._
import org.json4s.native.Serialization
import pn_challenge.utils.{CountryCode, PathConfig}

import scala.io.Source

// Task 1

// Reads events stored in JSON files

// Parser.clicks
// returns a list of Click case classes parsed from 4 files containing clicks in Json format

// Parser.impressions
// returns a list of Impression case classes parsed from 4 files containing impressions in Json format

object Parser {

  // case class to represent click
  case class Click(impressionId: UUID, revenue: Float)

  // case class to represent impression
  case class Impression(appId: Long, advertiserId: Long, countryCode: Option[CountryCode], id: UUID)

  // json4s serializer with serialization of UUID type as opposed to default format
  implicit lazy val formats: Formats = Serialization.formats(NoTypeHints) ++ org.json4s.ext.JavaTypesSerializers.all

  // reads text from file
  private def getText(path: String) = Source.fromFile(path).mkString

  // parses jsons and extracts them to a list of case classes
  private def readObjects[A](path: String)(implicit m: Manifest[A]): List[A] = Extraction.extract[List[A]] {
    jackson.parseJson(getText(path)).camelizeKeys
  }

  // goes through files and passes their paths to json reader method
  private def readFiles[A](files: List[File])(implicit m: Manifest[A]): List[A] =
    files.flatMap(file => readObjects(file.getPath))

  // reads all files in directory
  private def getListOfFiles(dir: String): List[File] = {
    val directory: File = new File(dir)

    if (directory.exists && directory.isDirectory)
      directory.listFiles.filter(_.isFile).toList
    else Nil
  }

  // list of click files in the corresponding directory
  private val clickFiles: List[File] = getListOfFiles(PathConfig.pathToClicks)
  // list of impression files in the corresponding directory
  private val impressionFiles: List[File] = getListOfFiles(PathConfig.pathToImpressions)

  // resulting clicks parsed to a list of Click case class
  val clicks: List[Click] = readFiles[Click](clickFiles)
  // resulting impressions parsed to a list of Impression case class
  val impressions: List[Impression] = readFiles[Impression](impressionFiles)

}
