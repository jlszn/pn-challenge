package pn_challenge.task2

import java.io.PrintWriter

import pn_challenge._
import pn_challenge.task1.JsonReader
import pn_challenge.utils.{PathConfig, ReaderWriterUtil}

object MetricsCalculator {

  // parsed clicks
  val clicks: List[Click] = JsonReader.parsedClicks(PathConfig.clicks)

  // parsed and filtered from extra data impressions
  val impressions: List[FilteredImpression] =
    JsonReader.parsedImpressions(PathConfig.impressions).collect {
      // filter out data without information on a country
      case i if i.countryCode.nonEmpty => FilteredImpression(i.appId, i.countryCode.get, i.id)
    }

  // impressions amount by key
  private def impressionsAmount: Map[Key, Int] =
    impressions.groupBy(i => (i.appId, i.countryCode)).map { case (k, v) => (k._1, k._2) -> v.length }

  // sum of clicks and revenues by key
  private def clicksAndRevenuesAmount: Map[Key, (Int, BigDecimal)] = {

    // list of impressions that have clicks (revenues)
    def revenuesWithImpressions: List[ImpressionAndRevenue] =
      for {impression <- impressions; cl <- clicks if impression.id == cl.impressionId}
        yield ImpressionAndRevenue(impression.appId, impression.countryCode, cl.revenue)

    // list of clicks (revenues) by key
    def revenues: Map[Key, List[BigDecimal]] =
      for ((k, v) <- revenuesWithImpressions.groupBy(z => (z.appId, z.countryCode))) yield (k._1, k._2) -> v.map(_.revenue)

    for ((k, v) <- revenues) yield (k._1, k._2) -> (v.length, v.sum)
  }

  // resulting calculations
  private def calculated: List[CalculatedRevenue] = impressionsAmount.flatMap {
    case (k, v) => clicksAndRevenuesAmount.get(k) match {
      case Some(key) => Some(CalculatedRevenue(k._1, k._2, v, key._1, key._2))
      case _ => None
    }
  }.toList

  def write(path: String): PrintWriter = ReaderWriterUtil.writeFile(calculated, path)

}
