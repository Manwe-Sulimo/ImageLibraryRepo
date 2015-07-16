package gg.lib.utils.kmeans

import gg.lib.linalg.general2.MatrixDense
import java.util.concurrent.Callable
import scala.reflect.ClassTag
import gg.lib.linalg.general.Ring
import scala.language.postfixOps
import gg.lib.utils.SetUtils

class AuxKMeans[T](matrix: MatrixDense[T], knots: Array[T], metric: (T, T) => Double)(implicit ring: Ring[T], f: ((T, Int), (T, Int)) => (T, Int), classTag: ClassTag[T]) extends Callable[Array[(T, Int)]] {
  override def call(): Array[(T, Int)] = {
    var accumulator = Array.fill(knots size)((ring.zero, 0))
    for (i <- 0 until matrix.m; j <- 0 until matrix.n) {
      accumulator = accumulator zip delta(matrix(i, j), knots) map { case (a, b) => f(a, b) }
    }
//    println(accumulator.foldLeft("acc [")((x, y) => x + "    " + y) + "    ]")
    accumulator
  }

  def delta: (T, Array[T]) => Array[(T, Int)] = {
    (cur, cmp) =>
      {
        val dist = cmp.map(x => metric(cur, x))
        val minDist = dist.reduce((a, b) => math.min(a, b))
        dist map { x => (minDist - x) < 0 match { case true => (ring.zero, 0); case false => (cur, 1) } }
      }
  }

}