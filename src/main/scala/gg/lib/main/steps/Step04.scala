package gg.lib.main.steps

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import scala.language.postfixOps
import gg.lib.linalg.general2.MatrixDense
import gg.lib.utils.components.ConnectedComponents
import gg.lib.utils.settings.Settings.maxThreads
import gg.lib.utils.kmeans.KMeans
import gg.lib.linalg.general.Ring
import gg.lib.utils.ImgUtils

class Step04(implicit ring: Ring[(Int, Int, Int)]) extends GreyStep {
  type rgb = (Int, Int, Int)

  def metric: (rgb, rgb) => Double = {
    (a, b) =>
      {
        val temp = ring.*(ring.-(a, b), ring.-(a, b))
        temp._1 + temp._2 + temp._3
      }
  }

  def predict(el: rgb, knots: Array[rgb]): Int = {
    val size = knots.length
    var index = 0
    var temp = 0.0
    for (i <- 0 until size) {
      if (metric(el, knots(i)) >= temp) {
        temp = metric(el, knots(i))
        index = i
      }
    }
    index * (255 / (size - 1))
  }

  implicit def f: ((rgb, Int), (rgb, Int)) => (rgb, Int) = {
    (a, b) => (ring.+(a._1, b._1), a._2 + b._2)
  }

  implicit def g: (rgb, (rgb, Int)) => rgb = {
    (x, a) =>
      {
        val b = if (a._2 == 0) 1 else a._2
        val tmp = ring.-(a._1, x)
        (tmp._1 / b, tmp._2 / b, tmp._3 / b)
      }
  }

  implicit def h: (rgb, Int) => rgb = {
    (x, a) =>
      {
        val b = if (a == 0) 1 else a
        (x._1 / b, x._2 / b, x._3 / b)
      }
  }

  override def compute(matrix: MatrixDense[Int]): MatrixDense[Int] = {
    val bgrMatrix = new MatrixDense(matrix.structElements map (_ map (x => { val tmp = ImgUtils.RGBA2intTuple(x); (tmp._1, tmp._2, tmp._3) })))

    val pool: ExecutorService = Executors.newFixedThreadPool(maxThreads)
    try {

      val startingCenters = Array(
        (0, 0, 0), (255, 0, 0), (0, 255, 0), (0, 0, 255),
        (50, 50, 50), (100, 100, 100), (150, 150, 150), (200, 200, 200),
        (120, 120, 60), (120, 60, 120), (60, 120, 120), (170, 170, 0))
      val centers = new KMeans(bgrMatrix, startingCenters, 10000, 0.0001, metric, pool).call()
      new MatrixDense(bgrMatrix.structElements map (_ map (x => predict(x, centers))))

    } finally {
      pool.shutdown()
    }
  }

}