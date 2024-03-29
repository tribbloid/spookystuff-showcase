package com.tribbloids.spookystuff.notebook

import ai.acyclic.prover.commons.spark.Envs
import com.tribbloids.spookystuff.SpookyContext
import com.tribbloids.spookystuff.actions.{Trace, Wget}
import com.tribbloids.spookystuff.extractors.{FR, GenExtractor}
import com.tribbloids.spookystuff.rdd.FetchedDataset
import com.tribbloids.spookystuff.showcase.SpookyRunnable

/**
  * move the entire webscraper.io/test-sites/ into a local dir for integration tests may use wayback machine:
  * https://web.archive.org/web/20170707111752/http://webscraper.io:80/test-sites
  */
// TODO: move to showcase notebook
object DownloadIntegrationSite extends SpookyRunnable {

  val SPLITTER: String = "/http://webscraper.io(:80)?"
  val SPLITTER_MIN: String = "/http://webscraper.io"

  import scala.concurrent.duration._
  val coolDown: Some[FiniteDuration] = Some(5.seconds)

  implicit class FDSView(fd: FetchedDataset) {

    import com.tribbloids.spookystuff.dsl.DSL._

    val pathTemplate: GenExtractor[FR, String] = S.uri
      .andMap { uri =>
        val base = uri.split(SPLITTER).last
        Envs.USER_TEMP_DIR :\ "test-sites" :\ base
      }

    def save(): FetchedDataset = {

      fd.persist()
      val originalVersion = fd.wget(
        S.uri.andMap { uri =>
          try {
            val Array(first, last) = uri.split(SPLITTER)
            first + "id_" + SPLITTER_MIN + last
          } catch {
            case e: Exception =>
              throw new UnsupportedOperationException(s"malformed URI: $uri", e)
          }
        },
        cooldown = coolDown
      )
      originalVersion
        .savePages_!(pathTemplate, overwrite = true)

      fd
    }
  }

  override def doMain(spooky: SpookyContext): Any = {

    import com.tribbloids.spookystuff.dsl.DSL._

    val keyBy: Trace => String = { trace =>
      val uri = trace.self
        .collectFirst {
          case wget: Wget => wget.uri.value
        }
        .getOrElse("")

      val base = uri.split(SPLITTER).last
      base

    }

    spooky
      .wget(
        "https://web.archive.org/web/20170707111752/http://webscraper.io:80/test-sites"
      )
      .save()
      .wgetFork(S"h2.site-heading a", cooldown = coolDown, keyBy = keyBy)
      .save()
      .wgetExplore(S"div.sidebar-nav a", cooldown = coolDown, keyBy = keyBy)
      .save()
      .wgetExplore(S"ul.pagination a", cooldown = coolDown, keyBy = keyBy)
      .save()
  }
}
