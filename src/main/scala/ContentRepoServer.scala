import unfiltered.request._
import unfiltered.response._
import unfiltered.jetty._
import unfiltered.scalate._
import org.apache.commons.configuration.{HierarchicalINIConfiguration}

object ContentRepoServer {
  var port = 8089

  // Echo Plan
  val echo = unfiltered.filter.Planify {
    //case Path(Seg(p :: Nil)) => ResponseString(p)
    case req => Ok ~> Scalate(req, "echo.ssp")
  }

  def main(args: Array[String]) {
    println("Starting server")
    println("Reading Configuration")
    readConfiguration
    Http(port)
    .context("/static") {
      _.resources(getClass().getResource("/static/"))
    }
    .filter(echo).run()
  }

  def readConfiguration() {
    val hic = new HierarchicalINIConfiguration("content_repo.config")
    println(hic.toString())
    println("HTTP port: "+hic.getString("http.port"))  
    port = hic.getInt("http.port");
  }

}
