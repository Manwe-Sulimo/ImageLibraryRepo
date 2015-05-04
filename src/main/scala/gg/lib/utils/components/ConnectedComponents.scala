package gg.lib.utils.components

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.FutureTask
import java.util.logging.Logger

import scala.language.postfixOps
import scala.reflect.ClassTag

import gg.lib.linalg.general.Ring
import gg.lib.linalg.general2.MatrixDense
import gg.lib.linalg.general2.MatrixDense.zeros
import gg.lib.utils.convolutions.LargeConvolution

/**
 * Callable for ConnectedComponents
 *
 * TODO implement multithreading?
 *
 * @author Manwe-Sulimo
 *
 */
class ConnectedComponents[T](matrix: MatrixDense[T], pool: ExecutorService)(implicit ring: Ring[T], classTag: ClassTag[T]) extends Callable[MatrixDense[Int]] {
  private val log: Logger = Logger.getGlobal()

  val q = 1
  val r = 1

  override def call(): MatrixDense[Int] = {
    val updateMatrix = zeros[Int](matrix.m + 2 * q, matrix.n + 2 * r)
    val mm = updateMatrix.m
    val nn = updateMatrix.n
    val checkMatrix = zeros[Int](mm, nn)

    // init updateMatrix
    for (i <- q until mm - q; j <- r until nn - r) {
      updateMatrix(i, j) = if (matrix(i - q, j - r) == ring.zero) 0 else 1 + (i - q) * matrix.n + j - r
    }

    val info = ConnectedComponents.splitComputations(mm, nn)
    var parity: Boolean = true

    do {
      checkMatrix(0 until mm toArray, 0 until nn toArray) = updateMatrix
      val subComputations = info.map {
        case (istart, iend, jstart, jend) => {
          val indexes = (istart + q, iend - q, jstart + r, jend - r)
          // submatrices shouldn't be tabbed (they are already tabbed)
          new FutureTask[Unit](new AuxConnected(indexes, updateMatrix, updateMatrix(istart to iend toArray, jstart to jend toArray), parity))
        }
      }

      subComputations.foreach(pool.execute(_))
      subComputations.foreach(_.get)

      parity = !parity
    } while (updateMatrix != checkMatrix)

    updateMatrix(q until mm - q toArray, r until nn - r toArray)
  }
}

/**
 * ConnectedComponents companion object
 */
object ConnectedComponents {

  // return info on how to split the matrix (which must be tabbed with ring.zero values). Splits should overlap
  def splitComputations(mm: Int, nn: Int): List[(Int, Int, Int, Int)] = {
    LargeConvolution.splitComputations(mm, nn, 1, 1)
  }

}