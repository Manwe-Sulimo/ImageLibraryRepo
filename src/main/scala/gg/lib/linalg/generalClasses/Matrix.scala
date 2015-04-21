package gg.lib.linalg.generalClasses

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
}

trait MatrixOps[T] {
  //aux methods
  def isSquare: Boolean = { ??? }
  def det: T = { ??? }
  def inverse: Matrix[T] = { ??? }
  def /(that: Matrix[T]): Matrix[T] = { ??? }
}