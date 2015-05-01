package gg.lib.utils.convolutions

import java.util.concurrent.Callable
import gg.lib.linalg.general2.MatrixDense
import java.util.logging.Logger
import gg.lib.utils.convolutions.MatrixUtils._
import scala.reflect.ClassTag
import gg.lib.linalg.general.Ring
import scala.language.postfixOps

/**
 * Callable for multithreading computing of matrix connected components
 *
 * @author Manwe-Sulimo
 *
 */
class AuxConnected[T](indexes: (Int, Int, Int, Int), updateMatrix: MatrixDense[T], mat: MatrixDense[T])(implicit ring: Ring[T], classTag: ClassTag[T], norm: T => Double) extends Callable[Unit] {
  private val log: Logger = Logger.getGlobal()
  private val (is, ie, js, je) = indexes

  def call(): Unit = {
    try {
      log.info("Starting computation of split: " + indexes)
      // the actual thing
      for (i <- 0 until mat.m - 2; j <- 0 until mat.n - 2) {
        if (mat(i + 1, j + 1) != ring.zero) {
          mat(i + 1, j + 1) = max(sub(mat, i + 1, j + 1, 1, 1))
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