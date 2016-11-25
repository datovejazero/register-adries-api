package eu.ideata.datahub.config

/**
  * Created by mbarak on 12/10/16.
  */
case class DatahubConfig(configPath: String = "/etc/datahub/web-api/app.conf")


object DatahubConfigParser {
  val OptionsParsers = new scopt.OptionParser[DatahubConfig]("scopt") {
    head("scopt", "3.x")

    opt[String]("config").action { case (x, c) => {
      c.copy(configPath = x)
    }}.text("Path to config")
  }

  def parse(params: Array[String]) = OptionsParsers.parse(params, DatahubConfig()).get
}