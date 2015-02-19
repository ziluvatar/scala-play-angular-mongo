name := """vessels-mng"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" % "bootstrap" % "3.3.2",
  "org.webjars" % "angularjs" % "1.3.13",
  "org.webjars" % "lodash" % "3.1.0",
  "org.webjars" % "angular-google-maps" % "2.0.11",
  "com.github.athieriot" %% "specs2-embedmongo" % "0.7.0" % "test",
  "org.mongodb" %% "casbah-core" % "2.7.2" % "test"
)

javaOptions in Test += "-Dconfig.file=conf/application-test.conf"
