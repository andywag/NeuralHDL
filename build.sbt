name := "scaladl"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq("org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
