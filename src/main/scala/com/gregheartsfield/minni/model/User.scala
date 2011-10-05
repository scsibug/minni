package com.gregheartsfield.minni.model

import com.gregheartsfield.minni.RedisStore
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import org.mindrot.jbcrypt._

class User (username: String, email: String) {
  var passhash = ""

  def toJson = ("username" -> username) ~ ("email" -> email) ~ ("passhash" -> passhash)

  def passwordValid(candidate: String) : Boolean = {
    BCrypt.checkpw(candidate, passhash)
  }

  def setPassword(pass: String) {
    passhash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
  }

  def url = "/users/"+username
}

object User {
  val r = RedisStore.getStore("main");
  def keyFromUsername(username: String) : String = "user."+username
  var email_set = "emails"

  // Check if user/email are unused
  def isUserAvailable(username: String, email: String) : Boolean = {
    // Valid usernames >=3 characters, and email/username not currently in use
    !r.exists(keyFromUsername(username)) &&
      (username.length>2) &&
      !r.sismember(email_set, email)
  }

  // retrieve a user by name
  def apply(username: String) : Option[User] = {
    // Retrieve from redis
    Some(new User(username, "from-redis"))
  }

  // Create a new user
  def apply(username: String, email: String, password: String) : Option[User] = {
    val u = new User(username, email)
    u.setPassword(password)
    if (r.setnx(keyFromUsername(username), compact(render(u.toJson)))) {
      r.sadd(email_set, email)
      Some(u)
    } else {
      None
    }
  }
}
