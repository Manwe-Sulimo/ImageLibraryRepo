package gg.lib.utils.components

import java.util.concurrent.Callable
import java.util.logging.Logger

import scala.language.postfixOps
import scala.reflect.ClassTag

import gg.lib.linalg.general.Ring
import gg.lib.linalg.general2.MatrixDense
import gg.lib.utils.convolutions.MatrixUtils.max
import gg.lib.utils.convolutions.MatrixUtils.sub

/**
 * Callable for multithreading computing of matrix connected components
 *
 * @author Manwe-Sulimo
 *
 */
class AuxConnected[T](indexes: (Int, Int, Int, Int), updateMatrix: MatrixDense[T], mat: MatrixDense[T], parity: Boolean)(implicit ring: Ring[T], classTag: ClassTag[T], norm: T => Double) extends Callable[Unit] {
  private val log: Logger = Logger.getGlobal()
  private val (is, ie, js, je) = indexes

  def call(): Unit = {
    try {
      log.info("Starting computation of split: " + indexes)
      // the actual thing
      // TODO: some refactoring
      if (parity) {
        for (i <- mat.m - 3 to 0 by -1; j <- mat.n - 3 to 0 by -1) {
          if (mat(i + 1, j + 1) != ring.zero) {
            mat(i + 1, j + 1) = max(sub(mat, i + 1, j + 1, 1, 1))
          }
        }
      } else {
        for (i <- 0 until mat.m - 2; j <- 0 until mat.n - 2) {
          if (mat(i + 1, j + 1) != ring.zero) {
            mat(i + 1, j + 1) = max(sub(mat, i + 1, j + 1, 1, 1))
          }
        }
      }
      updateMatrix(is to ie toArray, js to je toArray) = mat(1 until mat.m - 1 toArray, 1 until mat.n - 1 toArray)
      log.info("Ended computation of split: " + indexes)
    } catch {
      case e: Exception =>
        log.severe(e.getMessage() + "\t:\t" + "during computation of split: " + indexes)
        //handle otherwise
        throw e
    }
  }

}