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

/**
 * (Int, Int, Int) to Ring[(Int, Int, Int)] wrapper
 */
case class DSInt3(x: (Int, Int, Int)) extends Ring[(Int, Int, Int)] {
  //ring trait
  override def neg: DSInt3 = DSInt3((-x._1, -x._2, -x._3))
  override def +(that: Ring[(Int, Int, Int)]): DSInt3 = {
    val dat = that.get
    val res = (x._1 + dat._1, x._2 + dat._2, x._3 + dat._3)
    DSInt3(res)
  }
  override def *(that: Ring[(Int, Int, Int)]): DSInt3 = {
    val dat = that.get
    val res = (x._1 * dat._1, x._2 * dat._2, x._3 * dat._3)
    DSInt3(res)
  }
  override def get: (Int, Int, Int) = x

  //useful
  def toArray: Array[Int] = Array(x._1, x._2, x._3)
  def dotDiv(den: Int) = DSInt3(x._1 / den, x._2 / den, x._3 / den)
  def norm: Int = math.sqrt((toArray zip toArray).map { case (a, b) => a * b }.reduce((a, b) => a + b)).toInt
}



/**
 * (Double, Double, Double) to DSDouble3[(Double, Double, Double)] wrapper
 */
case class DSDouble3(x: (Double, Double, Double)) extends Ring[(Double, Double, Double)] {
  //ring trait
  override def neg: DSDouble3 = DSDouble3((-x._1, -x._2, -x._3))
  override def +(that: Ring[(Double, Double, Double)]): DSDouble3 = {
    val dat = that.get
    val res = (x._1 + dat._1, x._2 + dat._2, x._3 + dat._3)
    DSDouble3(res)
  }
  override def *(that: Ring[(Double, Double, Double)]): DSDouble3 = {
    val dat = that.get
    val res = (x._1 * dat._1, x._2 * dat._2, x._3 * dat._3)
    DSDouble3(res)
  }
  override def get: (Double, Double, Double) = x

  //useful
  def toArray: Array[Double] = Array(x._1, x._2, x._3)
  def dotDiv(den: Double) = DSDouble3(x._1 / den, x._2 / den, x._3 / den)
  def norm: Double = math.sqrt((toArray zip toArray).map { case (a, b) => a * b }.reduce((a, b) => a + b))
}