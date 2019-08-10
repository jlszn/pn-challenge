package pn_challenge.task1

import java.io.File

import org.json4s.native.Serialization
import org.json4s.{Extraction, Formats, NoTypeHints, native}
import pn_challenge.{Click, Impression}
import pn_challenge.utils.PathConfig

import scala.io.Source

// Task 1
// Read events stored in JSON files

// JsonParser.parsedClicks returns List[Click] - a list of clicks parsed and mapped to objects
// JsonParser.parsedImpressions returns List[Impression] - a list of impressions parsed and mapped to objects
object ParserJson4s extends App {

  // json4s serializer with serialization of UUID type as opposed to default format
  implicit lazy val formats: Formats = Serialization.formats(NoTypeHints) ++ org.json4s.ext.JavaTypesSerializers.all

  // reads text from file
  private def getText(path: String) = Source.fromFile(path).mkString

  // parses jsons and extracts them to a list of case classes
  private def readObjects[A](path: String)(implicit m: Manifest[A]): List[A] = Extraction.extract[List[A]] {
    native.parseJson(getText(path)).camelizeKeys
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
  private def clickFiles: List[File] = getListOfFiles(PathConfig.clicks)
  // list of impression files in the corresponding directory
  private def impressionFiles: List[File] = getListOfFiles(PathConfig.impressions)

  // resulting clicks parsed to a list of Click case class
  def parsedClicks: List[Click] = readFiles[Click](clickFiles)
  // resulting impressions parsed to a list of Impression case class
  def parsedImpressions: List[Impression] = readFiles[Impression](impressionFiles)

}
