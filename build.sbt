name := "content-repo"

organization := "com.gregheartsfield"

version := "0.1"

scalaVersion := "2.9.1"

resolvers += "Scala-Tools Nexus Repository for Releases" at "http://nexus.scala-tools.org/content/repositories/releases"

libraryDependencies += "junit" % "junit" % "4.8" % "test"

libraryDependencies += "net.databinder" %% "unfiltered" % "0.5.0"

libraryDependencies += "net.databinder" %% "unfiltered-filter" % "0.5.0"

libraryDependencies += "net.databinder" %% "unfiltered-jetty" % "0.5.0"

//https://github.com/debasishg/scala-redis.git
libraryDependencies += "net.debasishg" %% "redisclient" % "2.4.0"

libraryDependencies += "javax.servlet" % "servlet-api" % "2.3" % "provided"


