name := "pn-challenge"

version := "0.1"

scalaVersion := "2.13.0"

val circeVersion = "0.12.0-M4"

libraryDependencies ++= Seq(
//  "org.json4s" %% "json4s-jackson" % "3.6.7",

  "org.json4s" %% "json4s-native" % "3.6.7",
  "org.json4s" %% "json4s-ext" % "3.6.7",

  "com.typesafe" % "config" % "1.3.4"
)

