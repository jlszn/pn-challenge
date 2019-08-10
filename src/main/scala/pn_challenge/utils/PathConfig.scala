package pn_challenge.utils

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

// configuration for a safer setting and extraction of paths via application.conf
object PathConfig {

  private val config: Config = ConfigFactory.parseFile(new File("./src/main/resources/application.conf"))

  // get path to the resources directory from application.conf
  val resources: String = config.getString("path.resources")

  // get path to the clicks directory from application.conf
  val clicks: String = resources + config.getString("path.clicks")

  // get path to the impressions directory from application.conf
  val impressions: String = resources + config.getString("path.impressions")

  // get path to the output directory from application.conf
  val output: String = resources + config.getString("path.output")

  // get path of the revenue directory from application.conf
  val revenue: String = output + config.getString("path.revenue")


}