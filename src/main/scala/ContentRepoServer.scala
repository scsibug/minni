import unfiltered.request._
import unfiltered.response._
import unfiltered.jetty._
import unfiltered.scalate._
import org.apache.commons.configuration.{HierarchicalINIConfiguration}
import org.slf4j._

object ContentRepoServer {
  var port = 8089

  // Root Plan
//  val index = unfiltered.filter.Planify {
//    case req => Ok ~> Scalate(req, "index.ssp")
//  }

  var plans = Seq(new RootPlan)

  def applyPlans = plans.foldLeft(_: Server)(_ filter _)

  def main(args: Array[String]) {
    println("Starting server")
    println("Reading Configuration")
    readConfiguration
    applyPlans(Http(port)
               .context("/static") {
                 _.resources(getClass().getResource("/static/"))
               }).run()

  }

  def readConfiguration() {
    val hic = new HierarchicalINIConfiguration("content_repo.config")
    println(hic.toString())
    println("HTTP port: "+hic.getString("http.port"))  
    port = hic.getInt("http.port");
  }

}
