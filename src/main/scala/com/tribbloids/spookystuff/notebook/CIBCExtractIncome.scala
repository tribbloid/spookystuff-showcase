package com.tribbloids.spookystuff.notebook

import ai.acyclic.prover.commons.spark.Envs
import com.tribbloids.spookystuff.SpookyContext
import com.tribbloids.spookystuff.actions._
import com.tribbloids.spookystuff.dsl._
import com.tribbloids.spookystuff.showcase.SpookyRunnable

object CIBCExtractIncome extends SpookyRunnable {

  val file: String =
    Envs.USER_HOME :\ "Synced/Dropbox/__CORPORATE/BalanceSheet/DepositAccount_10012023.pdf"

//  val file =
//    Envs.USER_HOME :\ "Desktop" :\ "spark_survey.pdf"

  override def doMain(spooky: SpookyContext): Any = {

    spooky
      .fetch(
        Wget(file)
      )
      .extract(
        S"head title" ~ 'title,
        S.andMap(v => v.contentType.toString) ~ 'type,
        S.andMap(v => v.mimeType) ~ 'mime
      )
      .savePages_!(
        Envs.USER_HOME :\ "Desktop" :\ "__TEMP" :\ "xxx",
        overwrite = true,
        page = S
      )
      .savePages_!(
        Envs.USER_HOME :\ "Desktop" :\ "__TEMP" :\ "yyy",
        overwrite = true,
        page = S
          .andFlatMap { v =>
            v.converted.docForAuditing
          }
      )
      .toDF(sort = true)
      .show(false)
  }
}
