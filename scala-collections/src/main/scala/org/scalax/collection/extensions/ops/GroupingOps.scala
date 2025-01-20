package org.scalax.collection.extensions.ops

/**
 * Operations that yield groups of items from a source collection.
 *
 * @tparam T Type
 */
trait GroupingOps[T] extends Any {

  def splitWhen(pred: T => Boolean): List[List[T]]

  def grouper(size: Int, padValue: Option[T]): Iterator[Seq[T]]

}
