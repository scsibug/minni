package com.gregheartsfield.minni

// originally from https://gist.github.com/1152512

//import unfiltered.jetty._
import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._
import unfiltered.filter._
import unfiltered.Cookie
import org.slf4j._

import model.User

trait AuthService[T]{
  def auth(username: String, password: String): Option[T]
}

trait SessionStore[T, S]{
  def get(sid: S): Option[T]
  def put(data: T): S
}

case class UserSession(username: String)
object SimpleAuthService extends AuthService[UserSession]{
  def auth(username: String, password: String) = {
    User(username) filter { _ passwordValid password } map { _ => UserSession(username) }
  }
}

object RedisSessionStore extends SessionStore[UserSession, String] {
  val logger = LoggerFactory.getLogger(classOf[AuthPlan])
  val r = RedisStore.getStore("session");
  def get(sid: String) = r.get(sid) map {UserSession(_)}
  def put(data: UserSession) = {
    val sid = generateSid
    data match {
      case UserSession(u) => r.set(sid,u)
    }
    sid
  }

  protected def generateSid = scala.util.Random.alphanumeric.take(256).mkString
}

class AuthPlan extends Plan {
  // TODO: put in configuration file
  Config.sessionKey
  //val SESSION_KEY = "x2gmx3m0t723mgx40t7823mtgxo"
  val logger = LoggerFactory.getLogger(classOf[AuthPlan])

  def intent = {

    case Path("/secure") & Cookies(cookies) =>
      (for {
        sid <- cookies(Config.sessionKey)
        data <- RedisSessionStore.get(sid.value)
      } yield {
        ResponseString("Hello " + data.username)
      }) getOrElse {
        ResponseString("Go away!")
      }

    case req@GET(Path("/login")) =>
      Scalate(req, "login.scaml")

    case POST(Path("/login")) & Params(par) & Cookies(cookies) =>
      (for {
        user <- par("username").headOption
        pass <- par("password").headOption
        data <- SimpleAuthService.auth(user, pass)
      } yield {
        logger.info("Successful login for "+user)
        ResponseCookies(Cookie(Config.sessionKey, RedisSessionStore.put(data))) ~> Redirect("/secure")
      }) getOrElse {
        ResponseString("dupa")
      }

    // GET to /register to see the registration form
    case req@GET(Path("/register")) =>
      Scalate(req, "register.scaml")

    // POST to register to create a new account
    case POST(Path("/register")) & Params(par) & Cookies(cookies) =>
      (for {
        user <- par("username").headOption
        email <- par("email").headOption
        pass <- par("password").headOption
      } yield {
        // If username already exists or is invalid, send 409 conflict
        if (User.isUserAvailable(user, email)) {
          // Create account
          val u = User(user,email,pass)
          if (u.isDefined) {
            // Send 201 Created with user URL
            Created ~> Redirect(u.get url)
          } else {
            InternalServerError ~> ResponseString("Error creating user")
          }
        } else {
          Conflict ~> ResponseString("Username is invalid")
        }
      }) getOrElse {
        Conflict ~> ResponseString("Missing parameters")
      }
  }
}

object Authed {
  def unapply[T](r: HttpRequest[T]) = {
    val c = r match { case Cookies(cookies) => cookies }
    (for {
      sid <- c(Config.sessionKey)
      data <- RedisSessionStore.get(sid.value)
    } yield {
      data.username
    })
  }
  def apply[T](r: HttpRequest[T]): Option[String] = Authed.unapply(r)
}

