
val vs = versions()

dependencies {

    api(project(":parent:web"))

    implementation("org.apache.spark:spark-sql_${vs.scala.binaryV}:${vs.sparkV}")
    implementation("org.apache.spark:spark-mllib_${vs.scala.binaryV}:${vs.sparkV}")
}