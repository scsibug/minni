import unfiltered.request._
import unfiltered.response._
import unfiltered.jetty._
import unfiltered.scalate._
import org.apache.commons.configuration.{HierarchicalINIConfiguration}

object ContentRepoServer {
  var port = 8089

  // Echo Plan
  val index = unfiltered.filter.Planify {
    case req => Ok ~> Scalate(req, "index.ssp")
  }

  def main(args: Array[String]) {
    println("Starting server")
    println("Reading Configuration")
    readConfiguration
    Http(port)
    .context("/static") {
      _.resources(getClass().getResource("/static/"))
    }
    .filter(index).run()
  }

  def readConfiguration() {
    val hic = new HierarchicalINIConfiguration("content_repo.config")
    println(hic.toString())
    println("HTTP port: "+hic.getString("http.port"))  
    port = hic.getInt("http.port");
  }

}
