package com.tribbloids.spookystuff.showcase

import com.tribbloids.spookystuff.notebook.CIBCExtractData
import org.scalatest.funspec.AnyFunSpec

class RunnableTest extends AnyFunSpec {

  val runnables = Seq(
    CIBCExtractData
  )

  it("dummy") {
    println("dummy")
  }
  runnables.foreach { v =>
    it(v.appName) {

      v.main(Array.empty)
    }
  }
}
