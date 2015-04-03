package gg.lib.linalg
import scala.reflect.ClassTag

/**
 * Represents an m by n matrix: ${T} should at least be a ring i.e. a T(0,+,-,*) so that ${Matrix[T]} is a T-module
 */
trait Matrix[T] {

  def size: Product

  /*
   * unary ops
   */
  def neg: Matrix[T]

  /*
   * binary ops
   */
  def +(that: Matrix[T]): Matrix[T]
  def -(that: Matrix[T]): Matrix[T] = this + (that.neg)
  def *(that: Matrix[T]): Matrix[T]
  def *:(scalar: T): Matrix[T]

  /*
   * ------------------------------------------------------------------------------------------
   * 										Misc
   * ------------------------------------------------------------------------------------------ 
   */

  /*
   * transpose
   */
  def transpose: Matrix[T]

  /*
   * determinant
   */
  def det: T

  /*
   * p-norm
   */
  def norm(p: Int): T

  /*
   * convolution
   */
  def conv(that: Matrix[T], tabValue: T): Matrix[T]

  /*
   * map
   */
  def map[U <% Ring[U]](f: T => U)(implicit classTag: ClassTag[U]): Matrix[U]

  /*
   * reduce
   */

  def reduce(f: (T, T) => T): T

  /*
   * TODO: check if is 1 by 1 and extract -> implement as implicit somewhere
   */

}