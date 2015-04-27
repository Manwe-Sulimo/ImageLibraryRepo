package gg.lib.linalg.general

import gg.lib.linalg.general2.Complex

/**
 * Trait representing the concept of Field ( * non-commutative in general) (user is responsible of making sure that (T,+,*) is actually a Field)
 * Won't make a different trait for Field with * commutative etc
 *
 * @author Manwe-Sulimo
 *
 */
trait Field[@specialized(Int, Double, Long, Float) T] extends Ring[T] {
  //multiplicative inverse (checks and errors are in actual implementations)
  @throws[java.lang.ArithmeticException]("Trying to compute the inverse of 'zero'")
  def inverse(a: T): T

  //convenience method for a * inverse(b)
  @throws[java.lang.ArithmeticException]("Trying to divide by 'zero'")
  def /(a: T, b: T): T = *(a, inverse(b))
}

object Field {

  // Float field
  object FloatField extends Field[Float] {
    override def +(a: Float, b: Float): Float = a + b
    override def -(a: Float): Float = -a
    override def zero: Float = 0
    override def *(a: Float, b: Float): Float = a * b
    override def one: Float = 1
    override def inverse(a: Float): Float = if (a != 0) (1 / a) else throw new java.lang.ArithmeticException("Trying to compute the inverse of '0'")
  }

  // Double field
  object DoubleField extends Field[Double] {
    override def +(a: Double, b: Double): Double = a + b
    def +:(a: Double, b: Double): Double = a + b
    override def -(a: Double): Double = -a
    override def zero: Double = 0.0
    override def *(a: Double, b: Double): Double = a * b
    override def one: Double = 1.0
    override def inverse(a: Double): Double = if (a != 0) (1.0 / a) else throw new java.lang.ArithmeticException("Trying to compute the inverse of '0'")
  }

  // BigDecimal field
  object BigDecimalField extends Field[BigDecimal] {
    override def +(a: BigDecimal, b: BigDecimal): BigDecimal = a + b
    override def -(a: BigDecimal): BigDecimal = -a
    override def zero: BigDecimal = 0.0
    override def *(a: BigDecimal, b: BigDecimal): BigDecimal = a * b
    override def one: BigDecimal = 1.0
    override def inverse(a: BigDecimal): BigDecimal = if (a != 0) (1.0 / a) else throw new java.lang.ArithmeticException("Trying to compute the inverse of '0'")
  }

  // BigDecimal field
  object ComplexField extends Field[Complex] {
    override def +(a: Complex, b: Complex) = a + b
    override def -(a: Complex): Complex = -a
    override def zero: Complex = Complex(0.0, 0.0)
    override def *(a: Complex, b: Complex): Complex = a * b
    override def one: Complex = Complex(1.0, 0.0)
    override def inverse(a: Complex): Complex = if (a != Complex(0.0, 0.0)) a.inverse else throw new java.lang.ArithmeticException("Trying to compute the inverse of '0'")
  }
}