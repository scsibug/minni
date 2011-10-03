import unfiltered.request._
import unfiltered.response._
import unfiltered.jetty._
import unfiltered.scalate._
import org.apache.commons.configuration.{HierarchicalINIConfiguration}
import org.slf4j._

object ContentRepoServer {
  var port = 8089

  def main(args: Array[String]) {
    println("Starting server")
    println("Reading Configuration")
    readConfiguration
    // Configure AuthPlan
    var authplan = new AuthPlan
    // Order of plan evaluation
    var plans = Seq(new RootPlan,
                    authplan)
    def applyPlans = plans.foldLeft(_: Server)(_ filter _)
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
