package pn_challenge.task2

import pn_challenge.Checking.{ImpressionAndRevenue, Key}
import pn_challenge._
import pn_challenge.task1.ParserJson4s
import pn_challenge.utils.{CountryCode, PathConfig, WriterJson4s}

// Task 2

// Group by dimensions and calculate metrics
object MetricsCalculator extends App {

  case class CalculatedRevenue(appId: Long,
                               countryCode: Option[CountryCode],
                               impressions: Int,
                               clicks: Option[Int] = None,
                               revenue: Option[BigDecimal] = None)

  // get parsed clicks
  private val clicks: List[Click] = ParserJson4s.parsedClicks

  // get parsed impressions
  private val impressions: List[Impression] = ParserJson4s.parsedImpressions

  // filter impressions from the data that is not needed now (advertiser_id)
  private val filteredImpressions: List[FilteredImpression] = impressions.map(i => FilteredImpression(i.appId, i.countryCode, i.id))

  // impressions by key
  private def allImpressions: Map[Key, List[FilteredImpression]] = filteredImpressions.groupBy(i => (i.appId, i.countryCode))

  // impressions amount by key
  private def impressionsAmount: Map[Key, Int] = for {(k, v) <- allImpressions} yield (k._1, k._2) -> v.length

  // list of impressions that have clicks (revenues)
  private def revenuesWithImpressions: List[ImpressionAndRevenue] =
    for {
      impression <- filteredImpressions
      cl <- clicks if impression.id == cl.impressionId
    } yield
      ImpressionAndRevenue(impression.appId, impression.countryCode, cl.revenue)

  // list of clicks (revenues) by key
  private def revenues: Map[Key, List[BigDecimal]] =
    for {
      (k, v) <- revenuesWithImpressions.groupBy(z => (z.appId, z.countryCode))
    } yield
      (k._1, k._2) -> v.map(_.revenue)

  // sum of clicks and revenues by key
  private def clicksAndRevenues: Map[Key, (Int, BigDecimal)] =
    for {
      (k, v) <- revenues
    } yield
      (k._1, k._2) -> (v.length, v.sum)


  private def calculated: Map[Key, (Int, Option[Int], Option[BigDecimal])] =
    impressionsAmount.map {
      case (k, v) => (k, (v, clicksAndRevenues.get(k).map(_._1), clicksAndRevenues.get(k).map(_._2)))
    }

  private def calculatedConverted: List[CalculatedRevenue] =
    calculated.toList.map(a => CalculatedRevenue(a._1._1, a._1._2, a._2._1, a._2._2, a._2._3))

  def write = WriterJson4s.writeToFile(calculatedConverted, PathConfig.revenue)

}
