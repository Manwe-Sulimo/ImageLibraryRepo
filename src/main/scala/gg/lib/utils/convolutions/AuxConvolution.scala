package gg.lib.utils.convolutions

import java.util.concurrent.Callable
import gg.lib.linalg.general2.MatrixDense
import java.util.logging.Logger
import gg.lib.utils.convolutions.MatrixUtils._
import scala.reflect.ClassTag
import gg.lib.linalg.general.Ring
import scala.language.postfixOps

/**
 * Callable for multithreading matrix convolution
 *
 * @author Manwe-Sulimo
 *
 */
class AuxConvolution[T](indexes: (Int, Int, Int, Int), updateMatrix: MatrixDense[T], mat: MatrixDense[T], that: MatrixDense[T], doTab: Boolean = false, tabValue: Option[T] = None)(implicit ring: Ring[T], classTag: ClassTag[T]) extends Callable[Unit] {
  private val log: Logger = Logger.getGlobal()
  private val (is, ie, js, je) = indexes

  def call(): Unit = {
    try {
      log.info("Starting computation of split: " + indexes)
      val res = simpleConvolution(mat, that, doTab, tabValue);
      updateMatrix(is to ie toArray, js to je toArray) = res
      log.info("Ended computation of split: " + indexes)
    } catch {
      case e: Exception =>
        log.severe(e.getMessage() + "\t:\t" + "during computation of split: " + indexes)
      //handle otherwise
      //throw e
    }
  }

}