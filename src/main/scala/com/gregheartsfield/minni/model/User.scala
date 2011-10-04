package com.gregheartsfield.minni.model

import com.gregheartsfield.minni.RedisStore
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import org.mindrot.jbcrypt._

class User (username: String, email: String) {
  var passhash = ""

  def toJson = ("username" -> username) ~ ("email" -> email)

  def passwordValid(candidate: String) : Boolean = {
    BCrypt.checkpw(candidate, passhash)
  }

  def setPassword(pass: String) {
    passhash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
  }
}

object User {

  def keyFromUsername(username: String) : String = "user."+username

  def isUsernameValid(username: String) : Boolean = {
    val r = RedisStore.getStore("main");
    // Valid usernames >=3 characters, and not currently in use
    !r.exists(keyFromUsername(username)) && (username.length>2)
  }

  // retrieve a user by name
  def Apply(username: String) : Option[User] = {
    val r = RedisStore.getStore("main");
    // Retrieve from redis
    Some(new User(username, "from-redis"))
  }

}
