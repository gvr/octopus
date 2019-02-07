
lazy val commonSettings = Seq(
  organization := "cen.alpha",
  version := "0.1",
  scalaVersion := "2.12.8",
  scalacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-target:jvm-1.8",
    "-deprecation",
    "-feature",
    "-unchecked",
    //"-Xfatal-warnings",
    "-Xlint",
    "-Xfuture",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-unused"
  )
)

lazy val dockerSettings = Seq(
  dockerfile in docker := {
    // The assembly task generates a fat JAR file
    val artifact: File = assembly.value
    val artifactTargetPath = s"/app/${artifact.name}"

    new Dockerfile {
      from("openjdk:8-jre")
      add(artifact, artifactTargetPath)
      entryPoint("java", "-jar", artifactTargetPath)
    }
  },
  dockerExposedPorts := Seq(8080) // see application.conf; should be automatically related
)

lazy val octopus = (project in file("."))
  .enablePlugins(sbtdocker.DockerPlugin, JavaServerAppPackaging)
  .settings(commonSettings)
  .settings(dockerSettings)
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(
    name := "octopus",
    libraryDependencies ++= {
      val akkaActorVersion = "2.5.20"
      val akkaStreamVersion = "2.5.20"
      val akkaHttpVersion = "10.1.7"
      val configVersion = "1.3.3"
      val logbackVersion = "1.2.3"
      val scalaTestVersion = "3.0.5"
      Seq(
        "com.typesafe.akka" %% "akka-actor" % akkaActorVersion,
        "com.typesafe.akka" %% "akka-slf4j" % akkaActorVersion,
        "com.typesafe.akka" %% "akka-testkit" % akkaActorVersion % Test,
        "com.typesafe.akka" %% "akka-stream" % akkaStreamVersion,
        "com.typesafe.akka" %% "akka-stream-testkit" % akkaStreamVersion % Test,
        "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
        "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
        "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
        "com.typesafe" % "config" % configVersion,
        "ch.qos.logback" % "logback-classic" % logbackVersion,
        "org.scalatest" %% "scalatest" % scalaTestVersion % Test
      )
    }
  )

