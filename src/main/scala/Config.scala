import org.apache.commons.configuration.{HierarchicalINIConfiguration}

object Config {
  var hic = new HierarchicalINIConfiguration("content_repo.config")
  def httpPort = hic.getInt("http.port")
  def redisHost = hic.getString("redis.host")
  def redisPort = hic.getInt("redis.port")
  def redisMainDB = hic.getInt("redis.maindb")
  def redisSessionDB = hic.getInt("redis.sessiondb")
}
