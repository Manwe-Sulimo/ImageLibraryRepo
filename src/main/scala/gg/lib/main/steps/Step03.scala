package gg.lib.main.steps

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import scala.language.postfixOps

import gg.lib.linalg.general2.MatrixDense
import gg.lib.utils.convolutions.ConnectedComponents
import gg.lib.utils.convolutions.Settings.maxThreads
class Step03 extends GreyStep {

  override def compute(matrix: MatrixDense[Int]): MatrixDense[Int] = {
    val pool: ExecutorService = Executors.newFixedThreadPool(maxThreads)
    try {
      val temp = new ConnectedComponents(matrix, pool).call
      temp
      //TODO: some serious shit
    } finally {
      pool.shutdown()
    }
  }
  
}