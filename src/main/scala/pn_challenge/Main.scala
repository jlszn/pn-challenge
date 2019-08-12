package pn_challenge

import pn_challenge.task1.JsonReader
import pn_challenge.task2.MetricsCalculator
import pn_challenge.task3.Recommender
import pn_challenge.utils.PathConfig

object Main extends App {

  // Task 1
  val clicks: List[Click] = JsonReader.parsedClicks(PathConfig.clicks)
  val impressions: List[Impression] = JsonReader.parsedImpressions(PathConfig.impressions)

  // uncomment if you want to see the parsing results printed
  // clicks.foreach(println)
  // impressions.foreach(println)

  // Task 2
  MetricsCalculator.write(PathConfig.revenue) // to see the results go to /resources/output/calculated_revenue.json

  // Task 3
  Recommender.write(PathConfig.recommendation) // to see the results go to /resources/output/recommendation.json

}
