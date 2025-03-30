val toolkitV = "0.5.0"
val toolkit = "org.scala-lang" %% "toolkit" % toolkitV
val toolkitTest = "org.scala-lang" %% "toolkit-test" % toolkitV

val akkaVersion = "2.7.0"
val akkaHttpVersion = "10.5.0" 

ThisBuild / scalaVersion := "3.3.4"

dependencyOverrides += "org.scala-lang" % "scala3-library_3" % "3.3.4"

libraryDependencies ++= Seq(
  toolkit,
  toolkitTest % Test,
  "org.sangria-graphql" %% "sangria" % "4.2.5",   
  "org.sangria-graphql" %% "sangria-circe" % "1.3.2",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion cross CrossVersion.for3Use2_13,  //  Scala 3 conversion support
  "com.typesafe.akka" %% "akka-stream" % akkaVersion cross CrossVersion.for3Use2_13, //  Scala 3 conversion support
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion cross CrossVersion.for3Use2_13,  //  Scala 3 conversion support
  "io.circe" %% "circe-parser" % "0.14.1", 
  "io.circe" %% "circe-generic" % "0.14.1" 
)

// เพิ่ม repository เพื่อดึง dependencies ที่จำเป็น
resolvers += "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/releases/"

scalacOptions ++= Seq(
  "-Wunused:all",       // warning unused code
  "-deprecation",       // warning for deprecated code
  "-source:3.0-migration"
)
