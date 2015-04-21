package gg.lib.linalg.generalClasses

/**
 * Implementation of Complex numbers. instances are immutable
 *
 * @author Manwe-Sulimo
 * 
 */
case class Complex(re: Double, im: Double) {
  val norm = math.sqrt(re * re + im * im)

  def +(that: Complex) = Complex(re + that.re, im + that.im)
  def unary_- = Complex(-re, -im)
  def -(that: Complex) = this + -that
  def *(that: Complex) = Complex(re * that.re - im * that.im, re * that.im + im * that.re)
  def inverse: Complex = { Complex(re / norm, -im / norm); }
  def /(that: Complex) = this * that.inverse

}

object Complex {
  implicit def realToComplex: Double => Complex = x => Complex(x, 0)
  val i = Complex(0, 1)
}