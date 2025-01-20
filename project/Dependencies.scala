import sbt.*
import sbt.librarymanagement.ModuleID

object Dependencies {

  lazy val scalaLibs: Seq[ModuleID] = Seq()
  lazy val javaLibs: Seq[ModuleID] = Seq(
    "com.hierynomus" % "sshj" % "0.39.0"
  )

  def useJavaDeps(idxs: Int*): Seq[ModuleID] = idxs.map(idx => javaLibs(idx - 1))

  def useScalaDeps(idxs: Int*): Seq[ModuleID] = idxs.map(idx => scalaLibs(idx - 1))

}
