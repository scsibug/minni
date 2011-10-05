package com.gregheartsfield.minni.model

import com.gregheartsfield.minni.RedisStore
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import org.mindrot.jbcrypt._

class User (username: String, var email: String, var passhash: String) {

  def toJson = ("username" -> username) ~ ("email" -> email) ~ ("passhash" -> passhash)

  def passwordValid(candidate: String) : Boolean = {
    (passhash != "") && BCrypt.checkpw(candidate, passhash)
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
    val j = r.get(keyFromUsername(username))
    val u = for {
      JObject(child) <- parse (j getOrElse "")
      JField("username", JString(juser)) <- child
      JField("email", JString(jemail)) <- child
      JField("passhash", JString(jpasshash)) <- child
    } yield (new User(juser, jemail, jpasshash))
    u headOption
  }

  // Create a new user
  def apply(username: String, email: String, password: String) : Option[User] = {
    val u = new User(username, email, "")
    u.setPassword(password)
    if (r.setnx(keyFromUsername(username), compact(render(u.toJson)))) {
      r.sadd(email_set, email)
      Some(u)
    } else {
      None
    }
  }
}
