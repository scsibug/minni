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
  val creationDate = new java.util.Date
  val eTag = hashCode.toString

  val Head = Ok ~> Vary("Accept-Charset", "Accept-Encoding", "Accept-Language", "Accept")

  val Caching = 
    CacheControl("max-age=3600") ~> 
    LastModified(DateUtils.formatDate(creationDate)) ~> 
    ETag(eTag)

  def intent = {
    case OPTIONS(_) => Ok ~> Allow("GET", "HEAD", "OPTIONS")
    case HEAD(Path(Seg(Nil))) => {
      logger.debug("HEAD /")
      Head
    }
    case req @ GET(Path(Seg(Nil))) => {
      logger.debug("GET /")
      val cached =
        req match {
          case IfNoneMatch(xs) => xs contains eTag
          case IfModifiedSince(xs) => creationDate.after(xs)
          case _ => false
        }
      Head ~> Caching ~> (if (cached) NotModified else Scalate(req, "index.ssp"))
    }
  }
}
