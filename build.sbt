val toolkitV = "0.5.0"
val toolkit = "org.scala-lang" %% "toolkit" % toolkitV
val toolkitTest = "org.scala-lang" %% "toolkit-test" % toolkitV

val akkaVersion = "2.7.0"
val akkaHttpVersion = "10.5.0"  // ปรับเป็นเวอร์ชันที่รองรับ Scala 3

ThisBuild / scalaVersion := "3.3.4"

dependencyOverrides += "org.scala-lang" % "scala3-library_3" % "3.3.4"

libraryDependencies ++= Seq(
  toolkit,
  toolkitTest % Test,
  "org.sangria-graphql" %% "sangria" % "4.2.5",   // ใช้เวอร์ชันที่รองรับ Scala 3
  "org.sangria-graphql" %% "sangria-circe" % "1.3.2",  // รองรับ Circe ใน Scala 3
  "com.typesafe.akka" %% "akka-actor" % akkaVersion cross CrossVersion.for3Use2_13,  // ใช้เวอร์ชันที่รองรับ Scala 3
  "com.typesafe.akka" %% "akka-stream" % akkaVersion cross CrossVersion.for3Use2_13, // ใช้เวอร์ชันที่รองรับ Scala 3
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion cross CrossVersion.for3Use2_13,  // รองรับ Scala 3
  "io.circe" %% "circe-parser" % "0.14.1",  // รองรับ Scala 3
  "io.circe" %% "circe-generic" % "0.14.1"   // รองรับ Scala 3
)

// เพิ่ม repository เพื่อดึง dependencies ที่จำเป็น
resolvers += "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/releases/"

scalacOptions ++= Seq(
  "-Wunused:all",       // เปิดใช้งานการตรวจจับโค้ดที่ไม่ได้ใช้
  "-deprecation",       // แสดงคำเตือนเกี่ยวกับโค้ดที่เลิกใช้แล้ว
  "-source:3.0-migration" // ใช้โหมดช่วยปรับจาก Scala 2 ไปเป็น Scala 3
)
