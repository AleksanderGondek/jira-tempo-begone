lazy val root = (project in file("."))
  .settings(
    name := "jira_tempo_begone",
    version := "0.0.1",
    organization := "agondek",
    scalaVersion := "2.13.3",
    sbtVersion := "1.4.7",
 
    scalaSource in Compile := baseDirectory.value / "src/main",
    scalaSource in Test := baseDirectory.value / "src/test",

    scalacOptions ++= Seq(
      "-encoding",
      "utf8",
      "-Xsource:3",
      "-explaintypes",
      "-feature",
      "-language:existentials",
      "-language:experimental.macros",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Xcheckinit",
      "-Xfatal-warnings",
      "-Xlint:adapted-args",
      "-Xlint:constant",
      "-Xlint:delayedinit-select",
      "-Xlint:deprecation",
      "-Xlint:doc-detached",
      "-Xlint:inaccessible",
      "-Xlint:infer-any",
      "-Xlint:missing-interpolator",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      "-Wunused:nowarn",
      "-Wdead-code",
      "-Wextra-implicit",
      "-Wnumeric-widen",
      "-Wunused:implicits",
      "-Wunused:explicits",
      "-Wunused:imports",
      "-Wunused:locals",
      "-Wunused:params",
      "-Wunused:patvars",
      "-Wunused:privates",
      "-Wvalue-discard"
    ),

    libraryDependencies ++= Seq(
      "org.scalatest" % "scalatest_2.13" % "3.2.2" % "test",
    )

)
