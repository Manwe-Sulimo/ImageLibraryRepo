package gg.lib.utils.convolutions

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import org.scalatest.junit.JUnitRunner
import gg.lib.linalg.general2.MatrixDense
import gg.lib.utils.convolutions.MatrixUtils._
/**
 *
 * @author Manwe-Sulimo
 *
 */

@RunWith(classOf[JUnitRunner])
class MatrixUtilsTest extends FunSuite with Checkers {
  val els = Array(
    1, 1, 1, 1, 1,
    1, 0, 2, 3, 4,
    5, 6, 7, 8, 9)
  val mat = new MatrixDense(3, 5, els)

  test("MatrixUtils tab should work as expected") {
    val tels = Array(
      0, 0, 0, 0, 0, 0, 0,
      0, 1, 1, 1, 1, 1, 0,
      0, 1, 0, 2, 3, 4, 0,
      0, 5, 6, 7, 8, 9, 0,
      0, 0, 0, 0, 0, 0, 0)
    val tmat = new MatrixDense(5, 7, tels)
    assert(tab(mat, 1, 1, 0) == tmat)
  }

  test("MatrixUtils sub should work as expected") {
    val sels = Array(
      1, 1, 1,
      1, 0, 2,
      5, 6, 7)
    val sels2 = Array(5, 6, 7, 8, 9)
    val smat = new MatrixDense(3, 3, sels)
    val smat2 = new MatrixDense(1, 5, sels2)
    assert(sub(mat, 1, 1, 1, 1) == smat)
    assert(sub(mat, 2, 2, 0, 2) == smat2)
  }

  test("MatrixUtils dot should work as expected") {
    val dels = Array(
      1, 1,
      1, 0,
      5, 6)
    val dels2 = Array(
      1, 1,
      1, 0,
      5, 6)
    val dmat = new MatrixDense(3, 2, dels)
    val dmat2 = new MatrixDense(3, 2, dels2)
    assert(dot(dmat, dmat2) == 64)
  }

  test("MatrixUtils simpleConvolution should work as expected") {
    val cels = Array(
      1, 0, 0,
      0, 0, 0,
      0, 0, -1)
    val cels2 = Array(
      0, -2, -3, -4, 0,
      -6, -6, -7, -8, 1,
      0, 1, 0, 2, 3)
    val cels3 = Array(-6, -7, -8)
    val cmat = new MatrixDense(3, 3, cels)
    val cmat2 = new MatrixDense(3, 5, cels2)
    val cmat3 = new MatrixDense(1, 3, cels3)
    assert(simpleConvolution(mat, cmat, true) == cmat2)
    assert(simpleConvolution(mat, cmat, false) == cmat3)
  }
}
