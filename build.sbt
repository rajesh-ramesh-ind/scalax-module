import Dependencies._

import java.net.URL

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "scalax-module",
    description := "Extension module for Scala Language (unofficial)",
    developers := Developer("rajesh-ramesh-ind", "Rajesh Ramesh", "rajesh.ramesh.tn@proton.me", new URL("https://github.com/rajesh-ramesh-ind")) :: Nil,
    libraryDependencies ++= javaLibs
  ).aggregate(io, utils).dependsOn(io, utils).enablePlugins(JmhPlugin)

lazy val io = (project in file("scala-io"))
  .settings(
    name := "io",
    libraryDependencies ++= useJavaDeps(1)
  )

lazy val utils = (project in file("scala-utils"))
  .settings(
    name := "utils"
  )

lazy val collections = (project in file("scala-collections"))
  .settings(
    name := "collections"
  )