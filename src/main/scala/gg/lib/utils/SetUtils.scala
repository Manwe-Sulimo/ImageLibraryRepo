package gg.lib.utils

import gg.lib.linalg.DMatrix
import gg.lib.linalg.TotalOrder
import gg.lib.linalg.Intero
import scala.reflect.ClassTag
import gg.lib.linalg.Ring
import scala.util.Sorting
import gg.lib.linalg.DSInt3

trait SetUtils {
  /*
   * returns the cross product of two arrays (as a "sequence of rows")
   */
  def cross[T <: Any](rows: Array[T], columns: Array[T]): Array[(T, T)] = {
    val res = rows.flatMap(elr => columns.map(elc => (elr, elc)))
    res
  }

  /**
   * max in a matrix
   */
  def max[T <: TotalOrder[T]](matrix: DMatrix[T]): T = {
    matrix.collect.reduce((a, b) => TotalOrder.max(a, b))
  }

  /**
   * distance betweem two DSInt3 elements
   */
  def dist(a: DSInt3, b: DSInt3): Double = {
    math.sqrt(a.toArray.zip(b.toArray).map { case (a, b) => (a * a + b * b) }.reduce((a, b) => a + b))
  }

  /**
   * connected components on a '0-1 matrix'
   */
  def connectedComponents(mat: DMatrix[Int])(implicit f: Int => Intero) = {

    val height = mat.size._1
    val width = mat.size._2

    val stepR = 20
    val rs = Range.inclusive(2 * stepR, stepR + height - 1, (2 * stepR)).toArray
    val stepC = 20
    val cs = Range.inclusive(2 * stepC, stepC + width - 1, (2 * stepC)).toArray

    val knots = cross(rs, cs).reverse
    val tabbed: DMatrix[Int] = new DMatrix(height, width, mat.collect.zipWithIndex.map { case (a, b) => if (a == 0) 0 else b }).tab(stepR, stepC, 0)
    val tn = tabbed.size._1
    val tm = tabbed.size._2

    def iterate(oldMatrix: DMatrix[Int]): DMatrix[Int] = {
      // val computedMaxs = knots.map { case (i, j) => ((i, j), oldMatrix.subMatrix(i, j, stepR, stepC).reduce((a, b) => math.max(a, b))) }
      var newMatrix = new DMatrix(tn, tm, oldMatrix.collect.clone)
      knots.foreach {
        case (i, j) => {
          val k = newMatrix.subMatrix(i, j, stepR, stepC).reduce((a, b) => math.max(a, b))
          newMatrix = newMatrix.subMap(i, j, stepR, stepC, x => if (x == 0) 0 else k)
        }
      }
      if (newMatrix.equals(oldMatrix)) { newMatrix } else { iterate(newMatrix) }
    }

    val tempResult = iterate(tabbed)
    //    .collect
    //    Sorting.quickSort(tempResult)
    //    val result = tempResult.groupBy(x => x.get).zipWithIndex.map { case ((key, vals), index) => (index, vals.length) }

    tempResult
  }

}