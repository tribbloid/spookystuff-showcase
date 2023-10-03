
val vs = versions()

plugins {

    scala
    id("io.github.cosmicsilence.scalafix") version "0.1.14"
}

subprojects {
    apply(plugin = "scala")

    tasks {
        withType<ScalaCompile> {

            scalaCompileOptions.apply {
                loggingLevel = "verbose"

                val compilerOptions =

                    mutableListOf(

                        "-encoding", "UTF-8",
                        "-unchecked", "-deprecation", "-feature",

                        "-g:vars",

                        "-Xlint",
                    )

                additionalParameters = compilerOptions

                forkOptions.apply {

                    memoryInitialSize = "1g"
                    memoryMaximumSize = "4g"

                    // this may be over the top but the test code in macro & core frequently run implicit search on church encoded Nat type
                    jvmArgs = listOf(
                        "-Xss256m"
                    )
                }
            }
        }

        test {

            minHeapSize = "1024m"
            maxHeapSize = "4096m"

            testLogging {

                showExceptions = true
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

                showCauses = true
                showStackTraces = true

                // stdout is used for occasional manual verification
                showStandardStreams = true
            }

            useJUnitPlatform {
                includeEngines("scalatest")
                testLogging {
                    events("passed", "skipped", "failed")
                }

                val p = this@subprojects

                if (p.hasProperty("notLocal") ) {
                    excludeTags("com.tribbloids.spookystuff.testutils.LocalOnly")
                }
            }
        }
    }

    dependencies {

        api(project(":parent:web"))

        implementation("org.apache.spark:spark-sql_${vs.scala.binaryV}:${vs.sparkV}")
        implementation("org.apache.spark:spark-mllib_${vs.scala.binaryV}:${vs.sparkV}")
    }
}
