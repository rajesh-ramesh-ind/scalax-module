package org.scalax.collection.extensions.ops

trait TransformingOps[T] extends Any {
  def mapOnlyOn[U](p: T => Boolean)(f: T => U): List[U]
}
