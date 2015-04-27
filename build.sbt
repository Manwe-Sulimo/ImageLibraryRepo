lazy val commonSettings = Seq(
  organization := "gg.lib",
  version := "0.1.0",
  scalaVersion := "2.11.5"
)
lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "ImageLibrary",
	libraryDependencies ++= Seq(
	"org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
	"org.scalacheck" % "scalacheck_2.11" % "1.12.2" % "test",
	"junit" % "junit" % "4.12" % "test"
	),
	scalacOptions ++= Seq("-unchecked", "-feature")
  )