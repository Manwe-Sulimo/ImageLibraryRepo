package gg.lib.linalg.general2

import scala.reflect.ClassTag
import gg.lib.linalg.general.Ring
import scala.language.postfixOps
import gg.lib.linalg.errors.IncompatibleMatrixDimensionsException

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
  var structElements: Array[Array[T]] = Array.ofDim[T](m, n)
  if (elements.length != Array.empty[T].length) {
    require(mm * nn == elements.length)
    structElements = 0 until m map (i => 0 until n map (j => elements(i * n + j)) toArray) toArray
  }

  // --------------------------------------------------------------------------
  //
  // 				overloaded constructors: IT'S OVER 9000!
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
    require(rows.length == updateMatrix.m && columns.length == updateMatrix.n)
    for (i <- 0 until rows.length; j <- 0 until columns.length) {
      update(rows(i), columns(j), updateMatrix(i, j))
    }
  }
  def update(rows: Array[Int], columns: Array[Int], value: T): Unit = {
    for (i <- rows; j <- columns) {
      update(i, j, value)
    }
  }

  // convenience methods to extract/set rows or columns
  def row(i: Int): Array[T] = structElements(i)
  def updateRow(i: Int, newRow: Array[T]): Unit = this.structElements(i) = newRow
  def column(j: Int): Array[T] = 0 until m map (i => structElements(i)(j)) toArray
  def updateColumn(j: Int, newColumn: Array[T]): Unit = 0 until m foreach (i => this.structElements(i)(j) = newColumn(i))

  // --------------------------------------------------------------------------
  //
  //						matrix trait implementation
  // TODO: error handling
  // --------------------------------------------------------------------------

  override def +(that: Matrix[T]): MatrixDense[T] = {
    if (!canSum(that)) {
      throw new IncompatibleMatrixDimensionsException
    }
    val dat = that.toMatrixDense
    var temp = Array.ofDim[T](m, n)
    for (i <- 0 until m; j <- 0 until n) {
      temp(i)(j) = ring.+(this(i, j), dat(i, j))
    }
    new MatrixDense(temp)
  }
  override def unary_- : MatrixDense[T] = {
    var temp = Array.ofDim[T](m, n)
    for (i <- 0 until m; j <- 0 until n) {
      temp(i)(j) = ring.-(this(i, j))
    }
    new MatrixDense(temp)
  }
  override def -(that: Matrix[T]): MatrixDense[T] = {
    if (!canSum(that)) {
      throw new IncompatibleMatrixDimensionsException
    }
    val dat = that.toMatrixDense
    var temp = Array.ofDim[T](m, n)
    for (i <- 0 until m; j <- 0 until n) {
      temp(i)(j) = ring.-(this(i, j), dat(i, j))
    }
    new MatrixDense(temp)
  }
  override def *(that: Matrix[T]): MatrixDense[T] = {
    if (!canMultiply(that)) {
      throw new IncompatibleMatrixDimensionsException
    }
    val dat = that.toMatrixDense
    var temp = Array.ofDim[T](m, dat.n)
    for (i <- 0 until m; j <- 0 until dat.n) {
      temp(i)(j) = row(i).zip(dat.column(j)).map { case (a, b) => ring.*(a, b) }.reduce((a, b) => ring.+(a, b))
    }
    new MatrixDense(temp)
  }
  override def transpose: MatrixDense[T] = {
    var temp = Array.ofDim[T](n, m)
    for (i <- 0 until m; j <- 0 until n) {
      temp(j)(i) = this(i, j)
    }
    new MatrixDense(temp)
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
  //						hashCode, equals, toString 
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

/**
 * MatrixDense companion object
 */
object MatrixDense {
  def zeros[T](mm: Int, nn: Int)(implicit ring: Ring[T], classTag: ClassTag[T]): MatrixDense[T] = {
    new MatrixDense(Array.fill(mm, nn)(ring.zero))
  }
  def ones[T](mm: Int, nn: Int)(implicit ring: Ring[T], classTag: ClassTag[T]): MatrixDense[T] = {
    new MatrixDense(Array.fill(mm, nn)(ring.one))
  }
  def diag[T](mm: Int, nn: Int, value: T)(implicit ring: Ring[T], classTag: ClassTag[T]): MatrixDense[T] = {
    val res = zeros[T](mm, nn)
    for (i <- 0 until math.min(mm, nn)) {
      res(i, i) = value
    }
    res
  }
  // square diagonal matrix
  def diag[T](mn: Int, value: T)(implicit ring: Ring[T], classTag: ClassTag[T]): MatrixDense[T] = {
    diag[T](mn, mn, value)
  }
}