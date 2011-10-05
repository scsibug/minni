package com.gregheartsfield.minni

import unfiltered.filter._
import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._

import org.apache.http.impl.cookie.DateUtils
import org.slf4j._
import java.util.Date
import collection.immutable.Map

class RootPlan extends Plan {
  val logger = LoggerFactory.getLogger(classOf[RootPlan])

  val Head = Ok ~> Vary("Accept-Charset", "Accept-Encoding", "Accept-Language", "Accept")

  def intent = {
    case OPTIONS(_) => Ok ~> Allow("GET", "HEAD", "OPTIONS")
    case HEAD(Path(Seg(Nil))) => {
      logger.debug("HEAD /")
      Head
    }
    case req @ GET(Path(Seg(Nil))) => {
      logger.debug("GET /")
      Head ~> Scalate(req, "index.scaml")
    }
  }
}
