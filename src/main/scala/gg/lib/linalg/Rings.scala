package gg.lib.linalg

/*
 * Ring[T]
 */
trait Ring[T] {
  def neg: Ring[T]
  def +(that: Ring[T]): Ring[T]
  def -(that: Ring[T]): Ring[T] = this + (that.neg)
  def *(that: Ring[T]): Ring[T]
  def get: T
}

/**
 * Int to Ring[Int] wrapper
 */
case class Intero(x: Int) extends Ring[Int] with TotalOrder[Intero] {
  //ring trait
  def neg: Intero = Intero(-x)
  def +(that: Ring[Int]): Intero = Intero(this.get + that.get)
  def *(that: Ring[Int]): Intero = Intero(this.get * that.get)
  def get: Int = x
  //totalorder trait
  override def <(that: Intero) = x < that.get
}