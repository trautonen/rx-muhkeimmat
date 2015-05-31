name := "rx-muhkeimmat"
version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies += "io.reactivex" %% "rxscala" % "0.24.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

assemblyJarName in assembly := "rx-muhkeimmat.jar"
test in assembly := {}
