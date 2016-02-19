name := "headhunt"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.json"               % "json"               % "20090211",
  "org.jsoup"              % "jsoup"              % "1.8.3",
  "org.apache.spark"       %% "spark-core"        %   "1.5.1",
  "org.apache.spark"       %% "spark-sql"         %   "1.5.1",
  "org.apache.spark"       %% "spark-mllib"       %   "1.5.1",
  "org.apache.spark"       %% "spark-hive"        %   "1.5.1",
  "org.elasticsearch"       % "elasticsearch"     % "1.4.2",
  "log4j" % "log4j" % "1.2.17",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.7.1",
  "org.apache.hadoop" % "hadoop-core" % "1.2.1",
  "org.apache.hadoop" % "hadoop-client" % "2.4.0",
  "org.apache.hadoop" % "hadoop-hdfs" % "2.4.0",
  "org.mongodb" % "mongo-java-driver" % "2.11.2",
  "com.couchbase.client" % "couchbase-client" % "1.4.4"

)
