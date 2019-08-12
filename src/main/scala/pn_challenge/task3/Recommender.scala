package pn_challenge.task3

import java.io.PrintWriter

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import pn_challenge._
import pn_challenge.task1.JsonReader
import pn_challenge.utils.{PathConfig, ReaderWriterUtil}

object Recommender {

  private val spark: SparkSession =
    SparkSession
      .builder()
      .appName("pn-challenge")
      .master("local[8]")
      .getOrCreate()

  import spark.implicits._

  // parse clicks
  private def clicks: List[Click] = JsonReader.parsedClicks(PathConfig.clicks)

  // parse impressions
  private def impressions: List[Impression] = JsonReader.parsedImpressions(PathConfig.impressions)

  // map clicks to be less safe but easier to work with in DF
  private def clicksCompatible: List[ClickCompatible] = clicks.map(c => ClickCompatible(c.impressionId.toString, c.revenue))

  // map impressions to be less safe but easier to work with in DF
  private def impressionsCompatible: List[ImpressionCompatible] = impressions.collect {
    case i if i.countryCode.nonEmpty => ImpressionCompatible(i.appId, i.advertiserId, i.countryCode.get.toString, i.id.toString)
  }

  // convert clicks to DS
  private def clicksDS: Dataset[ClickCompatible] = spark.sparkContext.parallelize(clicksCompatible).toDS()

  // convert impressions to DS
  private def impressionsDS: Dataset[ImpressionCompatible] = spark.sparkContext.parallelize(impressionsCompatible).toDS()

  // get top advertisers
  private def topAdvertisers: DataFrame =
    impressionsDS.join(clicksDS, $"impressionId" === $"id")
      .drop("impressionId")
      .groupBy("appId", "countryCode", "advertiserId")
      .agg((sum("revenue") / count("id")) as "metrics")
      .sort(desc("metrics"))
      .groupBy("appId", "countryCode")
      .agg(collect_list($"advertiserId") as "recommendedAdvertiserIds".take(5))

  // convert to case classes to write them easily
  private def converted: List[Recommendation] = topAdvertisers.as[Recommendation].collect().toList

  // write recommendations to file
  def write(path: String): PrintWriter = ReaderWriterUtil.writeFile(converted, path)

}
