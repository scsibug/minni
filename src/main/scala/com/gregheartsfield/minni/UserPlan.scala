package com.gregheartsfield.minni

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._
import unfiltered.filter._
import unfiltered.Cookie
import org.slf4j._

import model.User

class UserPlan extends Plan {

  val logger = LoggerFactory.getLogger(classOf[UserPlan])

  def intent = {
    case req@GET(Path(Seg("users" :: username :: Nil))) => {
      Authed(req) match {
        case Some(auth_user) =>
          if (username == auth_user)
            Ok ~>ResponseString("You are authenticated as "+auth_user)
            //Ok ~> Scalate(req, "user.scaml")
          else
            Unauthorized ~> ResponseString("Please leave")
        case _ =>
          Redirect("/login")
      }
    }
  }
}
