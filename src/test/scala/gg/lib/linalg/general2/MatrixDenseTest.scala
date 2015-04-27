package gg.lib.linalg.general2

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import org.scalatest.junit.JUnitRunner

/**
 *
 * @author Manwe-Sulimo
 *
 */

@RunWith(classOf[JUnitRunner])
class MatrixDenseTest extends FunSuite with Checkers {

  test("Matrix creations, equals, selections should work as expected") {
    val els = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val r1 = Array(1, 2, 3)
    val r2 = Array(4, 5, 6)
    val r3 = Array(7, 8, 9)
    val r4 = Array(0, 0, 0)
    val r5 = Array(4, 0, 6)
    val c1 = Array(1, 4, 7)
    val c2 = Array(2, 5, 8)
    val c3 = Array(3, 6, 9)
    val s1 = Array(Array(4, 6))
    val s2 = Array(Array(1, 3), Array(7, 9))
    val m1 = new MatrixDense(1, 3, r1)
    val m2 = new MatrixDense(3, 1, c1)
    val m3 = new MatrixDense(s1)
    val m4 = new MatrixDense(s2)
    val mat1 = new MatrixDense(3, 3, els)
    val mat2 = new MatrixDense(Array(r1, r2, r3))
    val mat3 = new MatrixDense(Array(r1, r5, r3))
    val mat4 = new MatrixDense(Array(r4, r2, r3))
    //equals
    assert(mat1 == mat2)
    //canMultiply
    assert(!mat2.canMultiply(m1))
    assert(mat2.canMultiply(m2))
    assert(mat2.canSum(mat1))
    //canSum
    assert(!mat2.canSum(m1))
    assert(!mat2.canSum(m2))
    assert(mat2.canSum(mat1))
    //apply
    assert(mat2(0, 0) == 1)
    assert(mat2(Array(1), Array(0, 2)) == m3)
    assert(mat2(Array(0, 2), Array(0, 2)) == m4)
    assert(mat2.row(0).zip(r1).forall { case (a, b) => a == b })
    assert(mat2.column(1).zip(c2).forall { case (a, b) => a == b })
  }

  test("Matrix updates should work as expected") {
    val els = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val mat1 = new MatrixDense(3, 3, els)
    val mat2 = new MatrixDense(3, 3, els)
    //update element
    mat2(1, 1) = 0
    assert(mat2 == new MatrixDense(3, 3, Array(1, 2, 3, 4, 0, 6, 7, 8, 9)))
    // update elements: 1
    mat2(Array(0, 1, 2), Array(0, 1, 2)) = mat1
    assert(mat2 == mat1)
    //update row: 1
    mat2.updateRow(0, Array(0, 0, 0))
    mat2.updateRow(2, Array(0, 0, 0))
    assert(mat2 == new MatrixDense(3, 3, Array(0, 0, 0, 4, 5, 6, 0, 0, 0)))
    //update row: 2
    mat2(Array(0, 2), Array(0, 1, 2)) = new MatrixDense(2, 3, Array(1, 2, 3, 7, 8, 9))
    assert(mat2 == mat1)
    //update column: 1
    mat2.updateColumn(0, Array(0, 0, 0))
    mat2.updateColumn(1, Array(0, 0, 0))
    assert(mat2 == new MatrixDense(3, 3, Array(0, 0, 3, 0, 0, 6, 0, 0, 9)))
    //update colunm: 2
    mat2(Array(0, 1, 2), Array(0, 1)) = new MatrixDense(3, 2, Array(1, 2, 4, 5, 7, 8))
    assert(mat2 == mat1)
    //update elements: 2
    mat2(Array(0, 2), Array(0, 2)) = 0
    assert(mat2 == new MatrixDense(3, 3, Array(0, 2, 0, 4, 5, 6, 0, 8, 0)))
    mat2(Array(0), Array(0, 1, 2)) = new MatrixDense(1, 3, mat1.row(0))
    mat2.updateRow(1, Array(4, 5, 6))
    mat2.updateRow(2, Array(7, 8, 9))
    assert(mat2 == mat1)
    //multi updates
    val r = Array(0, 0, 0)
    val c = Array(1, 1, 1)
    val mat3 = new MatrixDense(3, 3, Array(0, 0, 0, 0, 0, 0, 0, 0, 0))
    val mat4 = new MatrixDense(3, 3, Array(1, 1, 1, 1, 1, 1, 1, 1, 1))
    mat2.updateRow(0, r)
    mat2.updateRow(1, r)
    mat2.updateRow(2, r)
    assert(mat2 == mat3)
    mat2(Array(0, 1, 2), Array(0)) = new MatrixDense(3, 1, c)
    mat2.updateColumn(1, c)
    mat2.updateColumn(2, c)
    assert(mat2 == mat4)
  }

  test("Matrix operations should work as expected") {
    val els1 = Array(
      1, 2, 3,
      4, 5, 6,
      7, 8, 9)
    val els2 = Array(
      0, 0, 1,
      0, 1, 0,
      1, 0, 0)
    val els3 = Array(
      3, 2, 1,
      6, 5, 4,
      9, 8, 7)
    val els4 = Array(
      2, 0,
      0, -1,
      -1, 0)
    val els5 = Array(
      -1, -2,
      2, -5,
      5, -8)
    val els6 = Array(
      1, 2,
      -2, 5,
      -5, 8)
    val els7 = Array(
      1, 2,
      3, 4,
      5, 6)
    val els8 = Array(
      1, 3, 5,
      2, 4, 6)
    val els9 = Array(
      1, 2, 2,
      4, 4, 6,
      6, 8, 9)
    val els10 = Array(
      1, 2, 4,
      4, 6, 6,
      8, 8, 9)
    val m1 = new MatrixDense(3, 3, els1)
    val m2 = new MatrixDense(3, 3, els2)
    val m3 = new MatrixDense(3, 3, els3)
    val m4 = new MatrixDense(3, 2, els4)
    val m5 = new MatrixDense(3, 2, els5)
    val m6 = new MatrixDense(3, 2, els6)
    val m7 = new MatrixDense(3, 2, els7)
    val m8 = new MatrixDense(2, 3, els8)
    val m9 = new MatrixDense(3, 3, els9)
    val m10 = new MatrixDense(3, 3, els10)
    assert(m1 * m2 == m3)
    assert(m1 * m4 == m5)
    assert(-m5 == m6)
    assert(m7.transpose == m8)
    assert(m1 - m2 == m9)
    assert(m1 + m2 == m10)
  }
}
