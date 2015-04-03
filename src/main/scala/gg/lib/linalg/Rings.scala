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
 * Field[T]
 */
trait Field[T] extends Ring[T] {
  override def neg: Field[T]
  override def +(that: Ring[T]): Field[T]
  override def -(that: Ring[T]): Field[T] = this + (that.neg)
  override def *(that: Ring[T]): Field[T]
  def inv: Field[T]
  def /(that: Field[T]): Field[T] = this * (that.inv)
}

/*
 * ---------------------------------------------------------------------------------------------------------------------------------
 * 													Concrete implementations
 * ---------------------------------------------------------------------------------------------------------------------------------
 */

/**
 * Int to Ring[Int] wrapper
 */
case class Intero(x: Int) extends Ring[Int] with TotalOrder[Intero] {
  //ring trait
  override def neg: Intero = Intero(-x)
  override def +(that: Ring[Int]): Intero = Intero(x + that.get)
  override def *(that: Ring[Int]): Intero = Intero(x * that.get)
  override def get: Int = x
  //total order
  override def <(that: Intero) = x < that.get
}

/**
 * Double to Field[Double] wrapper
 */
case class Decimale(x: Double) extends Field[Double] with TotalOrder[Decimale] {
  def neg: Decimale = Decimale(-x)
  def +(that: Ring[Double]) = Decimale(x + that.get)
  def *(that: Ring[Double]) = Decimale(x * that.get)
  def inv: Decimale = Decimale(1 / x)
  override def get = x
  //total order
  override def <(that: Decimale) = x < that.get
}


