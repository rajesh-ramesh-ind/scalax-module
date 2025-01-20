import scala.collection.mutable

object Main extends App {
  def hasDuplicate(lt: List[Int]): Boolean = {
    val set = mutable.HashSet[Int]()
    for (ele <- lt) {
      if (!set.add(ele)) {
        return true
      }
    }
    false
  }

  val lt = 1 :: 2 :: 2 :: 4 :: 5 :: Nil

  println(hasDuplicate(lt))

}

//import org.graalvm.compiler.graph.spi.Canonicalizable.Binary
//
//import java.io.{File, FileInputStream, FileOutputStream, FileWriter}
//import scala.io.Source
//import scala.util.Using
//
//trait DataWriter[I] {
//  def write(value: I): Unit
//}
//
//object Implicits {
//  implicit val fileWriter: DataWriter[String] = new DataWriter[String] {
//    def write(value: String)(implicit o: File): Unit = {
//      Using.resource(new FileWriter(o)) { res =>
//        res.write(value)
//      }
//    }
//  }
//  implicit val fileWriterBinary = new DataWriter[String, File] {
//    def write(value: String)(implicit o: File): Unit = {
//      Using.resource(new FileOutputStream(o)) { res =>
//        res.write(value.getBytes("UTF-8"))
//      }
//    }
//  }
//  implicit class StringExtensions(s: String) {
//    def >>(path: File)(implicit dw: DataWriter[String, File]): Unit = {
//      dw.write(s)(path)
//    }
//    def > (path: File)(implicit dw: DataWriter[String, File]): Unit = {
//      dw.write(s)(path)
//    }
//    def file = new File(s)
//  }
//  implicit class FileExtensions(f: File)
//}
//
//object Main extends App {
//
//  import Implicits._
//
//  "Hello World!" >> "sample.txt".file
//
//}