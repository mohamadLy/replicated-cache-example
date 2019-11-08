import jdk.nashorn.internal.runtime.NativeJavaPackage
import sbt.Resolver
import plugins._

name := "distributed-cache-demo"

version := "0.1"

scalaVersion := "2.12.8"

val akkaVersion = "2.5.25"
// https://mvnrepository.com/artifact/com.hazelcast/hazelcast
libraryDependencies ++=Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.1.7",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.7",
  "com.hazelcast" % "hazelcast" % "3.12",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
)
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
enablePlugins(AshScriptPlugin)

mainClass in Compile := Some("com.aruba.distributedcache.api.Server")
dockerBaseImage := "java:8-jre-alpine"
version in Docker := "0.1"
dockerExposedPorts := Seq(8000)
dockerRepository := Some("mohamadly")
