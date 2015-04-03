package gg.lib.linalg

trait TotalOrder[T] {
  def <(that: T): Boolean
  def <=(that: T): Boolean = (this < that) || (this equals that)
  def >(that: T): Boolean = !(this <= that)
  def >=(that: T): Boolean = !(this < that)
}
object TotalOrder {
  def max[T <: TotalOrder[T]](a: T, b: T): T = if (a < b) b else a
  def min[T <: TotalOrder[T]](a: T, b: T): T = if (a < b) a else b
}