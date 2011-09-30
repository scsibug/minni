import unfiltered.request._
import unfiltered.response._
import org.apache.commons.configuration.{HierarchicalINIConfiguration}

object ContentRepoServer {

  // Echo Plan
  val echo = unfiltered.filter.Planify {
    case Path(Seg(p :: Nil)) => ResponseString(p)
  }

  def main(args: Array[String]) {
    println("Starting server")
    println("Reading Configuration")
    readConfiguration
    unfiltered.jetty.Http.anylocal.filter(echo).run() 
  }

  def readConfiguration() {
    val hic = new HierarchicalINIConfiguration("content_repo.config")
    println(hic.toString())
    println("HTTP port: "+hic.getString("http.port"))
  }

}
