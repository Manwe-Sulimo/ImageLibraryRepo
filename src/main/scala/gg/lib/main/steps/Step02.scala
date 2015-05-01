package gg.lib.main.steps

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.logging.Logger

import gg.lib.linalg.general2.MatrixDense
import gg.lib.main.steps.MatrixDoubleUtils.binarize
import gg.lib.main.steps.MatrixDoubleUtils.zeroFiltering
import gg.lib.utils.convolutions.MatrixUtils.convolution
import gg.lib.utils.convolutions.Settings.maxThreads

class Step02 extends BinaryStep {
  private val log: Logger = Logger.getGlobal()

  override def compute(matrix: MatrixDense[Double]): MatrixDense[Double] = {
    val pool: ExecutorService = Executors.newFixedThreadPool(maxThreads)

    binarize(matrix, 0.0)

    val filter0: MatrixDense[Double] = new MatrixDense(3, 3, Array(
      1.0, 1.0, 1.0,
      1.0, 0.0, 1.0,
      1.0, 1.0, 1.0))

    //    val filter1: MatrixDense[Double] = new MatrixDense(3, 3, Array(
    //      1.0, 0.0, 0.0,
    //      0.0, 0.0, 0.0,
    //      0.0, 0.0, -1.0))
    //    val filter2: MatrixDense[Double] = new MatrixDense(3, 3, Array(
    //      0.0, 1.0, 0.0,
    //      0.0, 0.0, 0.0,
    //      0.0, -1.0, 0.0))
    //    val filter3: MatrixDense[Double] = new MatrixDense(3, 3, Array(
    //      0.0, 0.0, 1.0,
    //      0.0, 0.0, 0.0,
    //      -1.0, 0.0, 0.0))
    //    val filter4: MatrixDense[Double] = new MatrixDense(3, 3, Array(
    //      0.0, 0.0, 0.0,
    //      -1.0, 0.0, 1.0,
    //      0.0, 0.0, 0.0))

    try {

      val m0 = convolution(matrix, filter0, true, None, pool)
      zeroFiltering(m0, matrix, 7)
      val m1 = convolution(m0, filter0, true, None, pool)
      zeroFiltering(m1, m0, 1)
      val m2 = convolution(m1, filter0, true, None, pool)
      zeroFiltering(m2, m1, 2)

    } finally {
      pool.shutdown()
    }
  }
}