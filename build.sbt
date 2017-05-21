name := "scalahdl"

version := "1.0"

//scalaVersion := "2.12.2"
scalaVersion := "2.11.8"

val nd4jVersion = "0.7.2"

val vegas_version = "0.3.9"
/*
libraryDependencies ++= Seq("org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6" ,
  "org.scala-lang" % "scala-reflect" % "2.12.2" ,
  "org.scalatest" % "scalatest_2.12" % "3.0.1" % "test")
*/

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2"

libraryDependencies += "org.nd4j" % "nd4j-native-platform" % nd4jVersion

//libraryDependencies += "org.nd4j" % "nd4s_2.12.0-M3" % "0.4-rc3.8"
libraryDependencies += "org.nd4j" %% "nd4s" % nd4jVersion

libraryDependencies += "org.bytedeco" % "javacpp" % "1.3.2"

libraryDependencies += "org.jfree" % "jfreechart" % "1.0.14"

//

