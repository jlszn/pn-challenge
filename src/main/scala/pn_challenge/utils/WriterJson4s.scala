package pn_challenge.utils

import java.io.PrintWriter

import org.json4s.DefaultFormats
import org.json4s.native.Serialization.write

object WriterJson4s {

  implicit val formats: DefaultFormats.type = DefaultFormats

  private def writeToJson[A](o: A): String = write[A](o)

  def writeToFile[A](o: A, path: String): PrintWriter =
    new PrintWriter(path) {

      write {
        writeToJson(o)
      }

      close()
    }

}
