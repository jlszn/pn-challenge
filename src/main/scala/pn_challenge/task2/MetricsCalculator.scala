package pn_challenge.task2

import pn_challenge.task1.JsonParser
import pn_challenge.{Click, FilteredImpression, ImpressionAndRevenue, Key}

// Task 2

// Group by dimensions and calculate metrics
object MetricsCalculator {

  private val clicks: List[Click] = JsonParser.parsedClicks

  private val impressions: List[FilteredImpression] = JsonParser.parsedImpressions.map(i => FilteredImpression(i.appId, i.countryCode, i.id))

  def allImpressions: Map[Key, List[FilteredImpression]] = impressions.groupBy(i => (i.appId, i.countryCode))

  // IMPRESSIONS by key
  def impressionsByKey: Map[Key, Int] = for {(k, v) <- allImpressions} yield (k._1, k._2) -> v.length

  // (app_id, code, one revenue)
  def revenuesByKey: List[ImpressionAndRevenue] = {
    for {impression <- impressions; cl <- clicks if impression.id == cl.impressionId}
      yield ImpressionAndRevenue(impression.appId, impression.countryCode, cl.revenue)
  }

  // map(key -> revenues)
  def zippedGrouped: Map[Key, List[BigDecimal]] = {
    for {(k, v) <- revenuesByKey.groupBy(z => (z.appId, z.countryCode))}
      yield (k._1, k._2) -> v.map(_.revenue)
  }

  //CLICKS AND REVENUE
  def clicksAndRevenues: Map[Key, (Int, BigDecimal)] = {
    for {(k, v) <- zippedGrouped}
      yield (k._1, k._2) -> (v.length, v.sum)
  }

  def res: Map[Key, (Int, Option[Int], Option[BigDecimal])] =
    impressionsByKey.map {
      case (k, v) => (k, (v, clicksAndRevenues.get(k).map(_._1), clicksAndRevenues.get(k).map(_._2)))
    }

  def writeToJson = ???


}
