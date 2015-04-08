package gg.lib.linalg

trait TotalOrder[T] extends Ordered[T] {
  override def <(that: T): Boolean
  override def <=(that: T): Boolean = (this < that) || (this equals that)
  override def >(that: T): Boolean = !(this <= that)
  override def >=(that: T): Boolean = !(this < that)
  override def compare(that: T): Int = {
    if (<(that)) {
      -1
    } else if (>(that)) {
      1
    } else {
      0
    }
  }
}

object TotalOrder {
  def max[T <: TotalOrder[T]](a: T, b: T): T = if (a < b) b else a
  def min[T <: TotalOrder[T]](a: T, b: T): T = if (a < b) a else b
}