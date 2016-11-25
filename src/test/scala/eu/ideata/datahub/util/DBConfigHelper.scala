package eu.ideata.datahub.util

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by mbarak on 12/10/16.
  */
object DBConfigHelper {
  def testConfig: Config = ConfigFactory.parseFile(new File(getClass.getClassLoader.getResource("myConfig.conf").getFile))
}
