package gg.lib.main.steps

import gg.lib.linalg.general2.MatrixDense
import gg.lib.linalg.general2.MatrixDense._
import gg.lib.utils.convolutions.MatrixUtils.convolution
import java.util.logging.Logger
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import gg.lib.utils.convolutions.Settings.maxThreads
import gg.lib.main.steps.MatrixDoubleUtils._

class Step01 extends DefaultStep {
  private val log: Logger = Logger.getGlobal()

  override def compute(matrix: MatrixDense[Double]): MatrixDense[Double] = {
    val pool: ExecutorService = Executors.newFixedThreadPool(maxThreads)
    val filter0: MatrixDense[Double] = new MatrixDense(3, 3, Array(
      1.0, 1.0, 1.0,
      1.0, 1.0, 1.0,
      1.0, 1.0, 1.0))
    val filter1: MatrixDense[Double] = new MatrixDense(3, 3, Array(
      1.0, 0.0, 0.0,
      0.0, 0.0, 0.0,
      0.0, 0.0, -1.0))
    val filter2: MatrixDense[Double] = new MatrixDense(3, 3, Array(
      0.0, 1.0, 0.0,
      0.0, 0.0, 0.0,
      0.0, -1.0, 0.0))
    val filter3: MatrixDense[Double] = new MatrixDense(3, 3, Array(
      0.0, 0.0, 1.0,
      0.0, 0.0, 0.0,
      -1.0, 0.0, 0.0))
    val filter4: MatrixDense[Double] = new MatrixDense(3, 3, Array(
      0.0, 0.0, 0.0,
      -1.0, 0.0, 1.0,
      0.0, 0.0, 0.0))

    try {
      val m0 = convolution(convolution(matrix, filter0, false, None, pool), filter0, false, None, pool)
      val m1 = convolution(m0, filter1, false, None, pool)
      val m2 = convolution(m0, filter2, false, None, pool)
      val m3 = convolution(m0, filter3, false, None, pool)
      val m4 = convolution(m0, filter4, false, None, pool)
      val m5 = max(max(max(m1, m2), m3), m4)
      val meanValue = mean(m5)
      meanFiltering(m5, 2 * meanValue)
    } finally {
      pool.shutdown()
    }
  }

}