package gg.lib.linalg.general2

import gg.lib.linalg.general.Ring

/**
 * Representation of an m by n matrix
 *
 * @author Manwe-Sulimo
 *
 */
trait Matrix[T] {
  def +(that: Matrix[T]): Matrix[T]
  def unary_- : Matrix[T]
  def -(that: Matrix[T]): Matrix[T]
  def *(that: Matrix[T]): Matrix[T]
  def transpose: Matrix[T]

  //convenience method (shortens implementation of the above methods in classes wich actually implement this trait)
  def toMatrixDense: MatrixDense[T] = {
    this match {
      case _: MatrixDense[T] => this.asInstanceOf[MatrixDense[T]]
      case _ => { ??? }
    }
  }
}

trait MatrixOps[T] {
  //aux methods
  def isSquare: Boolean = { ??? }
  def det: T = { ??? }
  def inverse: Matrix[T] = { ??? }
  def /(that: Matrix[T]): Matrix[T] = { ??? }
}