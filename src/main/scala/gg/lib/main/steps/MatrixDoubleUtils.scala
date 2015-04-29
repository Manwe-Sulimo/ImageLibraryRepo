package gg.lib.main.steps

import gg.lib.linalg.general2.MatrixDense
import gg.lib.linalg.general2.MatrixDense._

object MatrixDoubleUtils {

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

  def binarize(mat: MatrixDense[Double], value: Double): MatrixDense[Double] = {
    for (i <- 0 until mat.m; j <- 0 until mat.n) {
      mat(i, j) = if (mat(i, j) > value) 1.0 else 0.0
    }
    mat
  }

  def zeroFiltering(mat: MatrixDense[Double], oldMat: MatrixDense[Double], value: Double): MatrixDense[Double] = {
    for (i <- 0 until mat.m; j <- 0 until mat.n) {
      mat(i, j) = if (mat(i, j) > value && oldMat(i, j) != 0.0) 1.0 else 0.0
    }
    mat
  }

}