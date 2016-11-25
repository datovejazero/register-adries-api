import sbtassembly.MergeStrategy

name := """register-adries-api"""
organization := "eu.ideata.datahub"
version := "1.0.0-SNAPSHOT"
scalaVersion := "2.11.8"

logLevel := Level.Info
cancelable in Global := true

resolvers ++= Seq(
  Resolver.sonatypeRepo("public"),
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/",
  "Artifactory" at "http://maven.ideata.eu/artifactory/libs-release"
)

libraryDependencies ++= {
  val akkaV = "2.4.7"
  val tsConfigVersion = "1.3.0"
  val scoptVersion = "3.5.0"
  val jodaTime = "2.9.4"
  val slick = "3.1.0"

  Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe" % "config" % tsConfigVersion,
    "com.github.scopt" %% "scopt" % scoptVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "ch.megard" %% "akka-http-cors" % "0.1.4",
    "com.typesafe.slick" %% "slick" % slick,
    "com.typesafe.slick" %% "slick-extensions" % slick,
    "com.typesafe.slick" %% "slick-hikaricp" % slick,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
    "com.microsoft.sqlserver" % "sqljdbc42" % "4.2",
    //testing deps
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test"
  )
}

dependencyOverrides += "joda-time" % "joda-time" % "2.8.2"

lazy val ITest = config("integration") extend(Test)
lazy val root = project.in(file(".")).configs(ITest) settings(inConfig(ITest)(Defaults.testTasks): _*)

testOptions in Test := Seq(Tests.Filter(_.endsWith("Test")))
testOptions in ITest := Seq(Tests.Filter(_.endsWith("IT")))

test in assembly := Seq(Tests.Filter(_.endsWith("Test")))

assemblyMergeStrategy in assembly := {
  case PathList("org", "joda", "time", "base", "BaseDateTime.class") => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.copy(`classifier` = Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)

publishMavenStyle := true

publishTo := {
  val artifactory = "http://maven.ideata.eu/artifactory"
  if (isSnapshot.value)
    Some("Artifactory realm" at artifactory +  "/libs-snapshot-local")
  else
    Some("Artifactory realm" at artifactory + "/libs-release-local")
}
credentials += Credentials(Path.userHome / ".sbt" / ".ideata-credentials")

