package pn_challenge.task1

import java.io.File

import pn_challenge.utils.ReaderWriterUtil
import pn_challenge.{Click, Impression}

object JsonReader {

  // goes through files and passes their paths to json reader method
  private def readFiles[A](files: List[File])(implicit m: Manifest[A]): List[A] =
    files.flatMap(file => ReaderWriterUtil.readFile(file.getPath))

  // reads all files in directory
  private def getListOfFiles(dir: String): List[File] = {
    val directory: File = new File(dir)

    if (directory.exists && directory.isDirectory)
      directory.listFiles.filter(_.isFile).toList
    else Nil
  }

  // resulting clicks parsed to a list of Click case class
  def parsedClicks(path: String): List[Click] = readFiles[Click](getListOfFiles(path))

  // resulting impressions parsed to a list of Impression case class
  def parsedImpressions(path: String): List[Impression] = readFiles[Impression](getListOfFiles(path))

}
