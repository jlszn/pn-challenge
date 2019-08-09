package pn_challenge.utils

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

object PathConfig {

  private val config: Config = ConfigFactory.parseFile(new File("./src/main/resources/application.conf"))

  val pathToClicks: String = config.getString("path.clicks")
  val pathToImpressions: String = config.getString("path.impressions")

}