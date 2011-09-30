import unfiltered.request._
import unfiltered.response._

object HelloWorld {

  val echo = unfiltered.filter.Planify {
    case Path(Seg(p :: Nil)) => ResponseString(p)
  }

  def main(args: Array[String]) {
    println("Hello, world!")
    println("Starting server")
    unfiltered.jetty.Http.anylocal.filter(echo).run() 
  }

}
