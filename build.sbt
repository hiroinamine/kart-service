name := """kart-service"""
organization := "org.hiroinamine"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion),
    buildInfoOptions += BuildInfoOption.BuildTime,
    buildInfoOptions += BuildInfoOption.ToJson,
    buildInfoPackage := "org.hiroinamine"
  )

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies += ehcache
// Json4s
libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.7"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "3.1.0" % Test
