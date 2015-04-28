package gg.lib.utils.convolutions

import gg.lib.linalg.general2.MatrixDense
import scala.reflect.ClassTag
import scala.language.postfixOps
import gg.lib.linalg.general.Ring
import java.util.concurrent.ExecutorService

/**
 * Object containing utilities to compute matrix convolutions
 *
 * @author Manwe-Sulimo
 *
 */
object MatrixUtils {

  //convenience method to call a LargeConvolution
  def convolution[T](matrix: MatrixDense[T], that: MatrixDense[T], doTab: Boolean = false, tabValue: Option[T] = None, pool: ExecutorService)(implicit ring: Ring[T], classTag: ClassTag[T]) = {
    new LargeConvolution[T](matrix, that, doTab, tabValue, pool).call
  }

  //simple convolution (single thread)
  //TODO: error handling
  def simpleConvolution[T](mat: MatrixDense[T], dat: MatrixDense[T], doTab: Boolean = false, tabValue: Option[T] = None)(implicit ring: Ring[T], classTag: ClassTag[T]): MatrixDense[T] = {
    val _tabValue: T = if (tabValue == None) ring.zero else tabValue.get
    val di = (dat.m - 1) / 2
    val dj = (dat.n - 1) / 2
    val actual = if (doTab) tab(mat, di, dj, _tabValue) else mat
    val res = MatrixDense.zeros[T](actual.m - 2 * di, actual.n - 2 * dj)
    for (i <- di until actual.m - di; j <- dj until actual.n - dj) {
      res(i - di, j - dj) = dot(sub(actual, i, j, di, dj), dat)
    }
    res
  }

  // --------------------------------------------------------------------------
  //
  //					 			aux methods
  // TODO: error handling
  // --------------------------------------------------------------------------

  // tab the given matrix
  def tab[T](mat: MatrixDense[T], di: Int, dj: Int, value: T)(implicit ring: Ring[T], classTag: ClassTag[T]): MatrixDense[T] = {
    val rA = Array.fill[T](mat.n + 2 * dj)(value)
    val cSE = Array.fill[T](dj)(value)
    val temp = Array.ofDim[T](mat.m + 2 * di, mat.n + 2 * dj)
    for (i <- 0 until (mat.m + 2 * di)) {
      if (i < di || i >= mat.m + di) {
        temp(i) = rA
      } else {
        temp(i) = Array.concat(cSE, mat.row(i - di), cSE)
      }
    }
    new MatrixDense[T](temp)
  }

  // extract a submatrix
  def sub[T](mat: MatrixDense[T], iC: Int, jC: Int, di: Int, dj: Int)(implicit ring: Ring[T], classTag: ClassTag[T]): MatrixDense[T] = {
    mat(iC - di to iC + di toArray, jC - dj to jC + dj toArray)
  }

  //dot prod
  def dot[T](mat: MatrixDense[T], dat: MatrixDense[T])(implicit ring: Ring[T], classTag: ClassTag[T]): T = {
    var res: T = ring.zero
    for (i <- 0 until mat.m; j <- 0 until mat.n) {
      res = ring.+(res, ring.*(mat(i, j), dat(i, j)))
    }
    res
  }

}