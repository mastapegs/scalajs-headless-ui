import org.scalajs.linker.interface.ModuleSplitStyle

addCommandAlias("fmtall", "; scalafmtAll; scalafmtSbt")
addCommandAlias("fixall", "; scalafixAll; fmtall")

ThisBuild / scalaVersion      := "2.13.18"
ThisBuild / version           := "0.1.0-SNAPSHOT"
ThisBuild / organization      := "com.example"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := "4.15.2"

val circeVersion = "0.14.15"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "ui-template-sandbox",
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("com.example")))
    },
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "com.raquo"     %%% "laminar"     % "17.2.1",
      "com.raquo"     %%% "waypoint"    % "10.0.0-M1",
      "org.scala-js"  %%% "scalajs-dom" % "2.8.1",
      "io.circe"      %%% "circe-core"    % circeVersion,
      "io.circe"      %%% "circe-generic" % circeVersion,
      "io.circe"      %%% "circe-parser"  % circeVersion,
      "org.scalameta" %%% "munit"       % "1.1.0" % Test
    ),
    testFrameworks += new TestFramework("munit.Framework")
  )
