package com.gregheartsfield.minni.model

import com.gregheartsfield.minni.RedisStore

class User (username: String, email: String, password: String) {
  
}

object User {

  def keyFromUsername(username: String) : String = "user."+username

  def isUsernameValid(username: String) : Boolean = {
    val r = RedisStore.getStore("main");
    // Valid usernames >=3 characters, and not currently in use
    // Query redis
    !r.exists(keyFromUsername(username)) && (username.length>2)
  }

  def Apply(username: String) : User = {
    val r = RedisStore.getStore("main");
    // Retrieve from redis
    new User(username, "from-redis", "from-redis")
  }
  
  def Apply(username: String, email: String, password: String) : User = {
    val r = RedisStore.getStore("main");
    // Retrieve from redis

    new User(username, email, password)
  }
}
