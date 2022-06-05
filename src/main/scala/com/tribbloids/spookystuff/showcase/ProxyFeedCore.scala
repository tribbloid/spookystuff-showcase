//package com.tribbloids.spookystuff.example
//
//import org.apache.spark.sql.DataFrame
//import com.tribbloids.spookystuff.SpookyContext
//import com.tribbloids.spookystuff.actions._
//import com.tribbloids.spookystuff.dsl._
//import com.tribbloids.spookystuff.session.ProxySetting
//
///**
// * Created by peng on 9/11/14.
// */
//abstract class ProxyFeedCore extends SpookyRunnable {
//
//  var proxyRDD: DataFrame = null
//
//  override def getSpooky(args: Array[String]): SpookyContext = {
//    val noProxy = super.getSpooky(args)
//
//    import sql.implicits._
//
//    proxyRDD = proxyRDD(noProxy)
//
//    val proxyRows = proxyRDD.select('IP, 'Port, 'Type)
//      .collect()
//
//    val proxies = proxyRows.map(row => ProxySetting(row.getString(0), Integer.parseInt(row.getString(1)), row.getString(2)))
//
//    val spooky = noProxy
//    spooky.conf.proxy = ProxyFactories.Random(proxies)
//    spooky
//  }
//
//  def proxyRDD(spooky: SpookyContext): DataFrame = {
//
//    import spooky.dsl._
//
//    val httpPageRowRDD = spooky
//      .fetch(
//        Visit("http://www.us-proxy.org/")
//          +> WaitForDocumentReady
//          +> Paginate(
//          "a.next:not([class*=ui-state-disabled])",
//          limit =15
//        ),
//        flattenPagesOrdinalKey = 'page
//      )
//      .flatSelect(S"table.dataTable tbody tr")(
//        A("td", 0).text as 'IP,
//        A("td", 1).text as 'Port,
//        A("td", 2).text as 'Code,
//        A("td", 3).text as 'Country,
//        A("td", 4).text as 'Anonymity,
//        A("td", 5).text as 'Google,
//        A("td", 6).text as 'Https,
//        A("td", 7).text as 'LastChecked,
//        "http" as 'Type
//      ).persist()
//
//    val socksPageRowRDD = spooky
//      .fetch(
//        Visit("http://www.socks-proxy.net/")
//          +> WaitForDocumentReady
//          +> Paginate(
//          "a.next:not([class*=ui-state-disabled])",
//          limit =15
//        ),
//        flattenPagesOrdinalKey = 'page
//      )
//      .flatSelect(S"table.dataTable tbody tr")(
//        A("td", 0).text as 'IP,
//        A("td", 1).text as 'Port,
//        A("td", 2).text as 'Code,
//        A("td", 3).text as 'Country,
//        A("td", 4).text as 'Version,
//        A("td", 5).text as 'Anonymity,
//        A("td", 6).text as 'Https,
//        A("td", 7).text as 'LastChecked,
//        "socks5" as 'Type
//      ).persist()
//
//    httpPageRowRDD.union(socksPageRowRDD)
//      .toDF()
//    //      .where(('Anonymity !== "transparent")&& 'Code.like("US"))
//  }
//}