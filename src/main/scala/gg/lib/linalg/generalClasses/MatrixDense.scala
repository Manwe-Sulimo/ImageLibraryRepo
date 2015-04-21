package gg.lib.linalg.generalClasses

import scala.reflect.ClassTag

import gg.lib.linalg.general.Ring

/**
 * Representation of an m by n dense matrix
 * Matrices are represented as row matrices
 *
 * TODO: error handling
 *
 * @author Manwe-Sulimo
 *
 */
class MatrixDense[T](mm: Int, nn: Int, elements: Array[T])(implicit ring: Ring[T], classTag: ClassTag[T]) extends Matrix[T] {

  val m = mm
  val n = nn
  var structElements: Array[Array[T]] = Array.empty[Array[T]]
  if (elements != Array.empty[T]) {
    require(mm * nn == elements.length)
    structElements = 0 until m map (i => 0 until n map (j => elements(i * n + j)) toArray) toArray
  }

  // --------------------------------------------------------------------------
  //
  // 				overloaded constructor: IT'S OVER 9000!
  // TODO: error handling
  // --------------------------------------------------------------------------

  def this(elements: Array[Array[T]])(implicit ring: Ring[T], classTag: ClassTag[T]) = {
    this(elements.length, elements(0).length, Array.empty[T])
    structElements = elements
  }

  // --------------------------------------------------------------------------
  //
  // 					 	get/set element(s) methods
  // TODO: error handling
  // --------------------------------------------------------------------------

  // single-value methods
  def apply(i: Int, j: Int): T = structElements(i)(j)
  def update(i: Int, j: Int, value: T): Unit = structElements(i)(j) = value

  //multi-values methods
  def apply(rows: Array[Int], columns: Array[Int]): MatrixDense[T] = {
    new MatrixDense[T](rows.map(i => columns.map(j => apply(i, j))))
  }
  def update(rows: Array[Int], columns: Array[Int], updateMatrix: MatrixDense[T]): Unit = {
    require(rows.length * columns.length == updateMatrix.m * updateMatrix.n)
    for (i <- rows; j <- columns) {
      update(i, j, updateMatrix(i, j))
    }
  }
  def update(rows: Array[Int], columns: Array[Int], value: T): Unit = {
    for (i <- rows; j <- columns) {
      update(i, j, value)
    }
  }

  // convenience methods to extract/set rows or columns
  def row(i: Int): Array[T] = structElements(i)
  def updateRow(i: Int, newRow: Array[T]): Unit = structElements(i) = newRow
  // --------------------------------------------------------------------------
  //
  //						matrix trait implementation
  // TODO: error handling
  // --------------------------------------------------------------------------

  override def +(that: Matrix[T]): MatrixDense[T] = {

    ???
  }
  override def unary_- : MatrixDense[T] = {

    ???
  }
  override def -(that: Matrix[T]): MatrixDense[T] = {

    ???
  }
  override def *(that: Matrix[T]): MatrixDense[T] = {

    ???
  }
  override def transpose: MatrixDense[T] = {

    ???
  }

  // --------------------------------------------------------------------------
  //
  //						matrix trait auxiliaries
  // TODO: error handling
  // --------------------------------------------------------------------------

  def canSum(that: Matrix[T]): Boolean = {
    if (!that.isInstanceOf[MatrixDense[T]]) {
      false
    } else {
      val dat = that.asInstanceOf[MatrixDense[T]]
      m == dat.m && n == dat.n
    }
  }

  def canMultiply(that: Matrix[T]): Boolean = {
    if (!that.isInstanceOf[MatrixDense[T]]) {
      false
    } else {
      val dat = that.asInstanceOf[MatrixDense[T]]
      n == dat.m
    }
  }

  // --------------------------------------------------------------------------
  //
  //							everything else 
  // TODO: error handling
  // --------------------------------------------------------------------------

  // hashcode
  override def hashCode(): Int = toString().hashCode()

  // equals
  override def equals(that: Any): Boolean = {
    if (!that.isInstanceOf[MatrixDense[T]]) {
      false
    } else {
      val dat = that.asInstanceOf[MatrixDense[T]]
      m == dat.m &&
        n == dat.n &&
        structElements.zip(dat.structElements).forall {
          case (a, b) => a.zip(b).forall {
            case (c, d) => c.equals(d)
          }
        }

    }
  }

  // tostring
  override def toString: String = structElements.map(row => row.mkString("[\t", "\t|\t", "\t]")).mkString("", "\n", "")
}

object MD {
  def main(args: Array[String]): Unit = {
    val els = Array(
      0, 1, 2, 3, 4,
      5, 6, 7, 8, 9)

    val m = new MatrixDense(2, 5, els)
    m(1, 3) = 10
    m.updateRow(1, Array[Int](11, 12, 13, 14, 15))

    val n = new MatrixDense(2, 5, els)
    n(1, 3) = 10
    n.updateRow(1, Array[Int](11, 12, 13, 14, 15))

    println(n.hashCode())
    println(m.hashCode())
    println(n.hashCode() - m.hashCode())
    println(n.equals(m))

    println(n)
    println(m)
  }
}