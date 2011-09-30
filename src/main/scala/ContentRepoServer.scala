import unfiltered.request._
import unfiltered.response._

object ContentRepoServer {

  // Echo Plan
  val echo = unfiltered.filter.Planify {
    case Path(Seg(p :: Nil)) => ResponseString(p)
  }

  def main(args: Array[String]) {
    println("Starting server")
    unfiltered.jetty.Http.anylocal.filter(echo).run() 
  }

}
