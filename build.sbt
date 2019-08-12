name := "pn-challenge"

version := "0.1"

scalaVersion := "2.12.0"

val json4sVersion = "3.6.7"
val sparkVersion = "2.4.3"

libraryDependencies ++= Seq(

  "org.json4s" %% "json4s-native" % json4sVersion,
  "org.json4s" %% "json4s-ext" % json4sVersion,

  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,

  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",

  "com.typesafe" % "config" % "1.3.4"

)

