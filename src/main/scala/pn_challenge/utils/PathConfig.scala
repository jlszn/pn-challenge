package pn_challenge.utils

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

// configuration for a safer setting and extraction of paths via application.conf
object PathConfig {

  private val config: Config = ConfigFactory.parseFile(new File("./src/main/resources/application.conf"))

  // get path of the clicks directory from application.conf
  val pathToClicks: String = config.getString("path.clicks")

  // get path of the impressions directory from application.conf
  val pathToImpressions: String = config.getString("path.impressions")

}