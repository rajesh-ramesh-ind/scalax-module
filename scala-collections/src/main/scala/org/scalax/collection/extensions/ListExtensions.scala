package org.scalax.collection.extensions

import org.scalax.collection.extensions.ops.{GroupingOps, TransformingOps}

import scala.annotation.tailrec


object ListExtensions {
  implicit class ListGroupingOps[T](val lt: List[T]) extends AnyVal with GroupingOps[T] {

    def splitWhen(pred: T => Boolean): List[List[T]] = {
      @tailrec
      def _split(col: List[T], temp: List[T], res: List[List[T]]): List[List[T]] = col match {
        case ::(head, tail) =>
          if (pred(head)) _split(tail, List.empty, res :+ (temp :+ head))
          else _split(tail, temp :+ head, res)
        case Nil => if (temp.nonEmpty) res :+ temp else res
      }

      _split(lt, List.empty, List.empty)
    }

    def grouper(size: Int, padValue: Option[T] = None): Iterator[List[T]] = {
      val groupedIterator = lt.iterator.grouped(size)
      (padValue match {
        case Some(value) => groupedIterator.withPadding(value)
        case None => groupedIterator.withPartial(false)
      }).map(_.toList)
    }
  }

  implicit class ListTransformingOps[T](val lt: List[T]) extends AnyVal with TransformingOps[T] {
    def mapOnlyOn[U](p: T => Boolean)(f: T => U): List[U] = lt.withFilter(p).map(f)
  }

}

//    // Not recommended to create the anonymous function inline
//    def filterAndCollectMultiple(filters: T => Boolean*): Map[T => Boolean, List[T]] = {
//      val results = filters.map(p => p -> mutable.ListBuffer.empty[T]).toMap
//      var elem: Option[T] = list.headOption
//      while (elem.nonEmpty) {
//        elem.foreach { e =>
//          filters
//            .find(_(e))
//            .foreach(f => {
//              results(f).append(e)
//            })
//          elem = list.tail.headOption
//        }
//      }
//      results.view.mapValues(_.toList).toMap
//    }
//
//  }


object DriverApp extends App {

  import ListExtensions._

  val lt = (1 to 10).toList
  println(lt.grouped(3).toList)
  println(lt.grouper(3).toList)


  def compoundInterest(principal: Float, interest: Float, period: Int): Float = {
    val rate = interest / 100
    (1 to period).foldLeft(principal) { (_, init) =>
      init * rate
    }
  }

  println(compoundInterest(25000, 6.5F, 5))
}

