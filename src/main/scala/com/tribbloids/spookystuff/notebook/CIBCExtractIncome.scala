package com.tribbloids.spookystuff.notebook

import ai.acyclic.prover.commons.spark.Envs
import com.tribbloids.spookystuff.SpookyContext
import com.tribbloids.spookystuff.actions._
import com.tribbloids.spookystuff.showcase.SpookyRunnable
import com.tribbloids.spookystuff.utils.CommonUtils

import java.io.File

object CIBCExtractIncome extends SpookyRunnable {

  val file: String =
    Envs.USER_HOME :\ "Synced/Dropbox/__CORPORATE/BalanceSheet/DepositAccount_10012023.pdf".replaceAllLiterally(
      "/",
      File.pathSeparator
    )

  override def doMain(spooky: SpookyContext): Any = {

    spooky
      .fetch(
        Wget(file)
      )
      .savePages_!(CommonUtils.\\\(Envs.USER_HOME, "Desktop", "yyy"), overwrite = true)
  }
}
