package com.tribbloids.spookystuff.notebook

import com.tribbloids.spookystuff.SpookyContext
import com.tribbloids.spookystuff.actions._
import com.tribbloids.spookystuff.showcase.SpookyRunnable

object CIBCExtractIncome extends SpookyRunnable {

  val file = {
    "file://~/Synced/Dropbox/__CORPORATE/BalanceSheet/DepositAccount_10012023.pdf"
  }

  override def doMain(spooky: SpookyContext): Any = {

    spooky
      .fetch(
        Wget(file)
      )
      .savePages_!("~/Desktop/yyy", overwrite = true)
  }
}
