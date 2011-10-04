package com.gregheartsfield.minni

import com.redis._
import scala.collection.mutable.{Map}
object RedisStore {
  private val stores = Map[String, RedisClient]()

  def getStore(id: String) {
    stores get id
  }

  def createStore(name: String, host: String, port: Int, database: Int) {
    val client = new RedisClient(host,port)
    client.select(database)
    RedisStore.synchronized {
      stores += (name -> client)
    }
    client
  }
}
