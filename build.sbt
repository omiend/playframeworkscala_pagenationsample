name := """page_nation_sample"""
version := "1.0"
scalaVersion := "2.11.2"
lazy val root = (project in file(".")).enablePlugins(PlayScala)
libraryDependencies ++= Seq(
   jdbc
  ,"com.typesafe.slick" % "slick_2.11" % "2.1.0"
  ,"com.typesafe.play" % "play-slick_2.11" % "0.8.0"
)     
