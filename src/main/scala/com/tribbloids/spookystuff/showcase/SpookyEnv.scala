package com.tribbloids.spookystuff.showcase

import com.tribbloids.spookystuff.SpookyContext
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by peng on 22/06/14. allowing execution as a main object and tested as a test class keep each test as small
  * as possible, by using downsampling & very few iterations
  */
trait SpookyEnv {

  val appName: String = this.getClass.getSimpleName.replace("$", "")

  val spark: SparkSession = {

    val conf: SparkConf = new SparkConf().setAppName(appName)

    val master = conf
      .getOption("spark.master") // if submitted directly, this will be the value
      .orElse(Option(System.getenv("MASTER")))
      .getOrElse(s"local[${Runtime.getRuntime.availableProcessors()},10]")

    conf.setMaster(master)

    SparkSession.builder().config(conf).appName(appName).getOrCreate()
  }
  lazy val sc: SparkContext = spark.sparkContext
  lazy val sql: SQLContext = spark.sqlContext

  def getSpooky(args: Array[String]): SpookyContext = {
    // args deliberately ignored for being hard to integrate

    val spooky = new SpookyContext(sql)

    spooky.dirConf

    spooky
  }
}
