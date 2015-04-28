package gg.lib.main.steps

import gg.lib.linalg.general2.MatrixDense
import gg.lib.linalg.general2.MatrixDense._
import gg.lib.utils.convolutions.MatrixUtils.convolution
import java.util.logging.Logger
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import gg.lib.utils.convolutions.Settings.maxThreads

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
      meanFiltering(m5, meanValue)
    } finally {
      pool.shutdown()
    }
  }

  //TODO real errors etc etc
  def max(mat1: MatrixDense[Double], mat2: MatrixDense[Double]): MatrixDense[Double] = {
    if (mat1.m != mat2.m || mat1.n != mat2.n) {
      throw new Exception("cacca")
    } else {
      val res = zeros[Double](mat1.m, mat1.n)
      for (i <- 0 until mat1.m; j <- 0 until mat1.n) {
        res(i, j) = math.max(math.abs(mat1(i, j)), math.abs(mat2(i, j)))
      }
      res
    }
  }
  def mean(mat: MatrixDense[Double]): Double = {
    mat.structElements.map(el => el.reduce((a, b) => a + b)).reduce((a, b) => a + b) / (mat.m * mat.n)
  }
  def meanFiltering(mat: MatrixDense[Double], value: Double): MatrixDense[Double] = {
    for (i <- 0 until mat.m; j <- 0 until mat.n) {
      mat(i, j) = if (mat(i, j) > value) 255.0 else 0.0
    }
    mat
  }
}