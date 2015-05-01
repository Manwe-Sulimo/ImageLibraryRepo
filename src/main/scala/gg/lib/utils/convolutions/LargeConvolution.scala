package gg.lib.utils.convolutions

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.FutureTask
import java.util.logging.Logger

import scala.language.postfixOps
import scala.reflect.ClassTag

import gg.lib.linalg.general.Ring
import gg.lib.linalg.general2.MatrixDense
import gg.lib.utils.SetUtils.cross
import gg.lib.utils.convolutions.MatrixUtils.tab
import gg.lib.utils.convolutions.Settings.colStep
import gg.lib.utils.convolutions.Settings.colThresh
import gg.lib.utils.convolutions.Settings.rowStep
import gg.lib.utils.convolutions.Settings.rowThresh

/**
 * Callable for multithreading matrix convolution
 *
 * @author Manwe-Sulimo
 *
 */
class LargeConvolution[T](matrix: MatrixDense[T], that: MatrixDense[T], doTab: Boolean = false, tabValue: Option[T] = None, pool: ExecutorService)(implicit ring: Ring[T], classTag: ClassTag[T]) extends Callable[MatrixDense[T]] {
  private val log: Logger = Logger.getGlobal()
  private val _tabValue = if (tabValue == None) ring.zero else tabValue.get

  // input matrix size
  val (im, in) = (matrix.m, matrix.n)
  // optional tab dimensions
  val (q, r) = ((that.m - 1) / 2, (that.n - 1) / 2)
  // pre-tab the matrix, if required
  val actual = if (doTab) tab(matrix, q, r, _tabValue) else matrix
  val (m, n) = (actual.m, actual.n)

  def call(): MatrixDense[T] = {

    val info: List[(Int, Int, Int, Int)] = LargeConvolution.splitComputations(m, n, q, r)

    val updateMatrix = MatrixDense.zeros(m - 2 * q, n - 2 * r)

    val subComputations = info.map {
      case (istart, iend, jstart, jend) => {
        val indexes = (istart, iend - 2 * q, jstart, jend - 2 * r)
        // submatrices shouldn't be tabbed (they are already tabbed)
        new FutureTask[Unit](new AuxConvolution(indexes, updateMatrix, actual(istart to iend toArray, jstart to jend toArray), that))
      }
    }

    subComputations.foreach(pool.execute(_))
    subComputations.foreach(_.get)

    updateMatrix
  }
}

/**
 * LargeConvolution companion object
 */
object LargeConvolution {

  // returns info on how to split an already tabbed matrix
  // TODO? equi-split
  // TODO: error handling
  def splitComputations(mm: Int, nn: Int, qq: Int, rr: Int): List[(Int, Int, Int, Int)] = {
    import gg.lib.utils.convolutions.Settings._

    //if matrix number of rows is too big, split it (splits must intersect adjacent splits)
    var istep = if (mm >= rowThresh) rowStep else mm
    var inodes = ((0 to mm by istep).toList).dropRight(1)
    val ilastIndex = inodes.length - 1

    //map the split index to the indexes of the first and last row (both inclusive) which should be included in that split
    val iN = inodes.zipWithIndex.map(el => el._2 match {
      case o if (ilastIndex == o) => (math.max(inodes(o) - qq, 0), mm - 1)
      case o => (math.max(inodes(o) - qq, 0), math.min(inodes(o + 1) + qq, mm) - 1)
    })

    //if matrix number of columns is too big, split it (splits must intersect adjacent splits)
    var jstep = if (nn >= colThresh) colStep else nn
    var jnodes = ((0 to nn by jstep).toList).dropRight(1)
    val jlastIndex = jnodes.length - 1

    //map the split index to the indexes of the first and last row (both inclusive) which should be included in that split
    val jN = jnodes.zipWithIndex.map(el => el._2 match {
      case o if (jlastIndex == o) => (math.max(jnodes(o) - rr, 0), nn - 1)
      case o => (math.max(jnodes(o) - rr, 0), math.min(jnodes(o + 1) + rr, nn) - 1)
    })

    cross(iN, jN).map { case (in, jn) => (in._1, in._2, jn._1, jn._2) }
  }

}