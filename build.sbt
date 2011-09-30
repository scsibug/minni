name := "content-repo"

organization := "com.gregheartsfield"

version := "0.1"

scalaVersion := "2.9.1"

resolvers += "Scala-Tools Nexus Repository for Releases" at "http://nexus.scala-tools.org/content/repositories/releases"

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.8" % "test",
  "net.databinder" %% "unfiltered" % "0.5.0",
  "net.databinder" %% "unfiltered-filter" % "0.5.0",
  "net.databinder" %% "unfiltered-jetty" % "0.5.0",
  "net.debasishg" %% "redisclient" % "2.4.0", //https://github.com/debasishg/scala-redis.git
  "commons-configuration" % "commons-configuration" % "1.7",
  "javax.servlet" % "servlet-api" % "2.3" % "provided",
  "org.slf4j" % "slf4j-jdk14" % "1.6.2"
)

