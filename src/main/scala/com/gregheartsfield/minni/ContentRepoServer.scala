package com.gregheartsfield.minni

import unfiltered.request._
import unfiltered.response._
import unfiltered.jetty._
import unfiltered.scalate._
import org.slf4j._

object ContentRepoServer {
  var port = 8089

  def main(args: Array[String]) {
    println("Starting server on port "+Config.httpPort)
    // Setup redis clients
    RedisStore.createStore("cache",
                           Config.redisHost,
                           Config.redisPort,
                           Config.redisSessionDB)
    RedisStore.createStore("main",
                           Config.redisHost,
                           Config.redisPort,
                           Config.redisMainDB)
    // Configure AuthPlan
    var authplan = new AuthPlan
    // Order of plan evaluation
    var plans = Seq(new RootPlan,
                    authplan)
    def applyPlans = plans.foldLeft(_: Server)(_ filter _)
    applyPlans(Http(Config.httpPort)
               .context("/static") {
                 _.resources(getClass().getResource("/static/"))
               }).run()
  }
}
