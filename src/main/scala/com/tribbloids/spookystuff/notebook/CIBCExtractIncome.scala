package com.tribbloids.spookystuff.notebook

import com.tribbloids.spookystuff.SpookyContext
import com.tribbloids.spookystuff.actions._
import com.tribbloids.spookystuff.showcase.SpookyRunnable
import com.tribbloids.spookystuff.utils.{CommonConst, CommonUtils}

object CIBCExtractIncome extends SpookyRunnable {

  val file =
    CommonUtils.\\\(CommonConst.USER_HOME, "Synced/Dropbox/__CORPORATE/BalanceSheet/DepositAccount_10012023.pdf")

  override def doMain(spooky: SpookyContext): Any = {

    spooky
      .fetch(
        Wget(file)
      )
      .savePages_!(CommonUtils.\\\(CommonConst.USER_HOME, "Desktop", "yyy"), overwrite = true)
  }
}
