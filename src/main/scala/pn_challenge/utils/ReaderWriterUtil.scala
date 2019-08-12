package pn_challenge.utils

import java.io.PrintWriter

import org.json4s.FieldSerializer.{renameFrom, _}
import org.json4s._
import org.json4s.ext.EnumNameSerializer
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import pn_challenge.{CalculatedRevenue, Recommendation}

import scala.io.Source

object ReaderWriterUtil {

  // formatter for serialization and deserialization of objects
  implicit lazy val formats: Formats = Serialization.formats(NoTypeHints) ++
    org.json4s.ext.JavaTypesSerializers.all + calculatedRevenueSerializer + new EnumNameSerializer(CountryCode) +
    new EnumNameSerializer(CountryCode) + recommendationSerializer

  // formatter to write objects in snake case
  private val calculatedRevenueSerializer: FieldSerializer[CalculatedRevenue] = FieldSerializer[CalculatedRevenue](
    renameTo("appId", "app_id") orElse renameTo("countryCode", "country_code"),
    renameFrom("app_id", "appId") orElse renameFrom("country_code", "countryCode"))

  // formatter to write objects in snake case
  private val recommendationSerializer: FieldSerializer[Recommendation] = FieldSerializer[Recommendation](
    renameTo("appId", "app_id") orElse renameTo("countryCode", "country_code") orElse
      renameTo("recom", "recommended_advertiser_ids"),

    renameFrom("app_id", "appId") orElse renameFrom("country_code", "countryCode") orElse
      renameFrom("recommended_advertiser_ids", "recom")
  )

  def readFile[A](path: String)(implicit m: Manifest[A]): List[A] = {

    def getText: String = Source.fromFile(path).mkString

    Extraction.extract[List[A]] {
      native.parseJson(getText).camelizeKeys
    }
  }

  def writeFile[A](o: A, path: String): PrintWriter = {

    def writeToJson: String = write[A](o)

    new PrintWriter(path) {

      write {
        writeToJson
      }

      close()
    }
  }

}
