package gg.lib.linalg

import scala.reflect.ClassTag
import scala.collection.mutable.ArrayBuilder
import gg.lib.linalg.errors.IndexIncompatibleWithMatrixDimensionException
import gg.lib.linalg.errors.IncompatibleMatrixTypeException
import gg.lib.linalg.errors.IncompatibleMatrixDimensionsException
/**
 * Discrete Matrix implementation: row(0) :: row(1) :: ... :: row(m-1)
 */
class DMatrix[T <% Ring[T]](m: Int, n: Int, elements: Array[T])(implicit classTag: ClassTag[T]) extends Matrix[T] {

  require(elements.length == (m * n), "Exactly " + (m * n) + " elements are required, but elements.lenght was " + elements.length)

  def collect: Array[T] = elements

  /**
   * Indexes start at 0
   * @throws IndexOutOfBoundsException
   */
  def apply(x: Int): T = elements(x)

  def apply(i: Int, j: Int): T = {
    if (i < 0 || i >= m || j < 0 || j >= n) throw new IndexIncompatibleWithMatrixDimensionException("Attempting to access index " + (i, j) + " in matrix with size " + size)
    else {
      apply(i * n + j)
    }
  }

  /*
   * ---------------------------------------------------------------------------------------------------------------------------------
   * ---------------------------------------------------------------------------------------------------------------------------------
   */

  //TODO handle exceptions
  def row(i: Int): DMatrix[T] = {
    //invalid row index => throw exception
    if (i >= m) throw new IndexIncompatibleWithMatrixDimensionException("Received row index " + i + " on matrix with " + m + " rows")
    //otherwise return the selected row
    val els = elements.slice(i * n, i * n + n)
    new DMatrix[T](1, n, els)
  }

  //TODO handle exceptions
  def column(j: Int): DMatrix[T] = {
    //invalid column index => throw exception
    if (j >= n) throw new IndexIncompatibleWithMatrixDimensionException("Received column index " + j + " on matrix with " + n + " columns")
    //otherwise return the selected column
    val els = List.range(j, m * n + j, n).map(index => this(index)).toArray
    new DMatrix[T](m, 1, els)
  }

  //TODO handle exceptions
  def removeRow(i: Int): DMatrix[T] = {
    //invalid row index => throw exception
    if (i >= m) throw new IndexIncompatibleWithMatrixDimensionException("Received row index " + i + " on matrix with " + m + " rows")
    //otherwise remove the selected row and return the result
    val els = List.range(i * n, i * n + n)
    val res = elements.zipWithIndex.filterNot { case (el, ind) => els.contains(ind) }.map { case (el, ind) => el }.toArray
    new DMatrix[T](m - 1, n, res)
  }

  //TODO handle exceptions
  def removeColumn(j: Int): DMatrix[T] = {
    //invalid column index => throw exception
    if (j >= n) throw new IndexIncompatibleWithMatrixDimensionException("Received column index " + j + " on matrix with " + n + " columns")
    //otherwise remove the selected column and return the result
    val els = List.range(j, m * n + j, n)
    val res = elements.zipWithIndex.filterNot { case (el, ind) => els.contains(ind) }.map { case (el, ind) => el }.toArray
    new DMatrix[T](m, n - 1, res)
  }

  /**
   * select submatrix
   */
  def subMatrix(is: Array[Int], js: Array[Int]): DMatrix[T] = {
    val resM = is.length
    val resN = js.length
    val els = for (i <- is; j <- js) yield {
      apply(i, j)
    }
    new DMatrix[T](resM, resN, els)
  }

  def subMatrix(i: Int, j: Int, dr: Int, dc: Int): DMatrix[T] = {
    val is = ((i - dr) to (i + dr)).toArray
    val js = ((j - dc) to (j + dc)).toArray
    subMatrix(is, js)
  }

  /**
   * map submatrix / update
   */
  def subMap(is: Array[Int], js: Array[Int], f: T => T): DMatrix[T] = {
    val mappedEls = elements.clone
    for (i <- is; j <- js) {
      mappedEls(i * n + j) = f(mappedEls(i * n + j))
    }
    new DMatrix(m, n, mappedEls)
  }

  def subMap(i: Int, j: Int, dr: Int, dc: Int, f: T => T): DMatrix[T] = {
    val is = ((i - dr) to (i + dr)).toArray
    val js = ((j - dc) to (j + dc)).toArray
    subMap(is, js, f)
  }

  /**
   * tabs the matix's left and right side with $value s
   * ie: add columns
   */

  def tabC(dc: Int, value: T): DMatrix[T] = {
    val resM = m
    val resN = n + 2 * dc
    transpose.tabR(dc, value).transpose
  }

  /**
   * tabs the matix's top and bottom side with $value s
   * ie: add rows
   */
  def tabR(dr: Int, value: T): DMatrix[T] = {
    if (dr == 0) {
      this
    } else {
      val resM = m + 2 * dr
      val resN = n
      val tabRows = Array.fill(n * dr)(value)

      val builder: ArrayBuilder[T] = ArrayBuilder.make[T]
      builder.sizeHint(resM * resN)
      builder ++= tabRows
      builder ++= collect
      builder ++= tabRows

      new DMatrix[T](resM, resN, builder.result)
    }
  }

  /**
   * tabs the matix  with $value s
   */
  def tab(dr: Int, dc: Int, value: T): DMatrix[T] = {
    tabR(dr, value).tabC(dc, value)
  }

  /*
   * ---------------------------------------------------------------------------------------------------------------------------------
   * ---------------------------------------------------------------------------------------------------------------------------------
   */

  override def size = (m, n)

  /**
   * opposite matrix
   */
  override def neg = new DMatrix(m, n, elements.map(el => el.neg.get))

  /**
   * element-wise addition
   * @throws IncompatibleMatrixDimensionsException,IncompatibleMatrixTypeException
   */
  override def +(that: Matrix[T]): DMatrix[T] = {
    val clazz1 = classOf[DMatrix[T]]
    val clazz2 = that.getClass()
    that match {
      case dm: DMatrix[T] => {
        val dat = that.asInstanceOf[DMatrix[T]]
        //if matrix sizes are not compatible throw an exception
        if (size != dat.size) {
          throw new IncompatibleMatrixDimensionsException(size, dat.size)
        }
        val resArray = elements.zip(dat.collect).map(el => (el._1 + el._2).get)
        new DMatrix(m, n, resArray)
      }
      case _ => {
        // can not add matrices with "incompatible" class types => throw an exception
        throw new IncompatibleMatrixTypeException(clazz1, clazz2)
      }
    }
  }

  /**
   * (m by n) * (n by l)
   * @throws IncompatibleMatrixDimensionsException,IncompatibleMatrixTypeException
   */
  override def *(that: Matrix[T]): DMatrix[T] = {
    val clazz1 = classOf[DMatrix[T]]
    val clazz2 = that.getClass()
    that match {
      case dm: DMatrix[T] => {
        val dat = that.asInstanceOf[DMatrix[T]]
        //if matrix sizes are not compatible throw an exception
        if (size._2 != dat.size._1) {
          throw new IncompatibleMatrixDimensionsException(size, dat.size)
        }
        // otherwise return the matrix resulting from the product
        val resM = m
        val resN = dat.size._2
        val builder: ArrayBuilder[T] = ArrayBuilder.make[T]
        builder.sizeHint(resM * resN)
        for (i <- 0 until resM; j <- 0 until resN) {
          val r = this.row(i).collect
          val c = dat.column(j).collect
          val rc: T = r.zip(c).map { case (a, b) => a * b }.reduce((x, y) => x + y).get
          builder += rc
        }
        new DMatrix[T](resM, resN, builder.result)
      }
      case _ => {
        // can not multiply matrices with "incompatible" class types => throw an exception
        throw new IncompatibleMatrixTypeException(clazz1, clazz2)
      }
    }
  }

  /**
   * scalar multiplication
   */
  def *:(scalar: T): Matrix[T] = {
    new DMatrix(m, n, elements.map(el => (scalar * el).get))
  }

  /**
   * transpose matrix
   */
  override def transpose = {
    val builder: ArrayBuilder[T] = ArrayBuilder.make[T]
    builder.sizeHint(m * n)
    for (j <- 0 until n) {
      builder ++= this.column(j).collect
    }
    new DMatrix(n, m, builder.result)
  }

  /**
   * determinant
   */
  override def det = { ??? }

  /**
   * p-norm
   */
  override def norm(p: Int) = { ??? }

  /**
   * "convolution" product with $that
   * nb: that.size should be (2*h+1,2*k+1)
   */
  override def conv(that: Matrix[T], tabValue: T): DMatrix[T] = {
    val clazz1 = classOf[DMatrix[T]]
    val clazz2 = that.getClass()
    that match {
      case dm: DMatrix[T] => {
        val dat = that.asInstanceOf[DMatrix[T]]
        val dr = (dat.size._1 - 1) / 2
        val dc = (dat.size._2 - 1) / 2
        val tabbedMatrix = tab(dr, dc, tabValue)
        val builder: ArrayBuilder[T] = ArrayBuilder.make[T]
        builder.sizeHint(m * n)

        for (i <- dr until (dr + m); j <- dc until (dc + n)) {
          builder += tabbedMatrix.subMatrix(i, j, dr, dc).dotProd(dat)
        }

        new DMatrix[T](m, n, builder.result)
      }
      case _ => {
        //matrix types are incompatible => throw exception
        throw new IncompatibleMatrixTypeException(clazz1, clazz2)
      }
    }
  }

  /**
   * 'helper' for already tabbed matrix
   * "convolution" product with $that
   * nb: that.size should be (2*h+1,2*k+1)
   */
  def tabbedConv(that: Matrix[T]): DMatrix[T] = {
    val clazz1 = classOf[DMatrix[T]]
    val clazz2 = that.getClass()
    that match {
      case dm: DMatrix[T] => {
        val dat = that.asInstanceOf[DMatrix[T]]
        val dr = (dat.size._1 - 1) / 2
        val dc = (dat.size._2 - 1) / 2
        val newM = (m - 2 * dr)
        val newN = (n - 2 * dc)
        val builder: ArrayBuilder[T] = ArrayBuilder.make[T]
        builder.sizeHint(newM * newM)

        for (i <- dr until (m - dr); j <- dc until (n - dc)) {
          builder += subMatrix(i, j, dr, dc).dotProd(dat)
        }

        new DMatrix[T](newM, newN, builder.result)
      }
      case _ => {
        //matrix types are incompatible => throw exception
        throw new IncompatibleMatrixTypeException(clazz1, clazz2)
      }
    }
  }
  /*
   * ---------------------------------------------------------------------------------------------------------------------------------
   * 														UTILS
   * ---------------------------------------------------------------------------------------------------------------------------------
   */

  /**
   * dotProd
   */
  def dotProd(that: DMatrix[T]): T = {
    //TODO errors etc
    if (n != that.size._1) {
      throw new IncompatibleMatrixDimensionsException(size, that.size)
    }
    elements.zip(that.collect).map { case (a, b) => a * b }.reduce((x, y) => (x + y)).get
  }

  /*
   * ---------------------------------------------------------------------------------------------------------------------------------
   * 													maps & co
   * ---------------------------------------------------------------------------------------------------------------------------------
   */
  override def map[U <% Ring[U]](f: T => U)(implicit classTag: ClassTag[U]): DMatrix[U] = {
    new DMatrix[U](m, n, elements.map(f))
  }

  override def reduce(f: (T, T) => T): T = {
    elements.reduce(f)
  }

  override def equals(that: Any) = {
    if (that.isInstanceOf[DMatrix[T]]) {
      val dat = that.asInstanceOf[DMatrix[T]]
      if (size != dat.size) {
        false
      } else {
        collect.zip(dat.collect).forall(x => (x._1).equals(x._2))
      }
    } else {
      false
    }
  }

  override def toString() = {
    val builder: StringBuilder = new StringBuilder
    for (i <- 0 until m) {
      val pref = "Line " + i + ", columns 0 to " + (n - 1) + ":\n\t"
      val suff = row(i).collect.mkString("[\t", "\t,\t", "\t]\n")
      builder ++= pref ++= suff
    }
    builder.result
  }
}