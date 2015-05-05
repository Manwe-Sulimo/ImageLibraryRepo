package gg.lib.utils.kmeans

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.FutureTask

import scala.language.postfixOps
import scala.reflect.ClassTag

import gg.lib.linalg.general.Ring
import gg.lib.linalg.general2.MatrixDense
import gg.lib.utils.convolutions.LargeConvolution

/**
 * KMeans for matrices
 *
 * @author Manwe-Sulimo
 *
 * TODO: input validation and error handling
 * TODO: KMeans with given starting centers
 * TODO: testing
 */
class KMeans[T](matrix: MatrixDense[T], numKnots: Int, numIters: Int, eps: Double, metric: (T, T) => Double, pool: ExecutorService)(implicit f: ((T, Int), (T, Int)) => (T, Int), g: (T, (T, Int)) => T, ring: Ring[T], classTag: ClassTag[T]) extends Callable[Array[T]] {

  private var counter = 0

  private def isResultBad(arr1: Array[T], arr2: Array[T]): Boolean = {
    counter = counter + 1
    val subRes = for (i <- 0 until arr1.length) yield {
      metric(arr1(i), arr2(i))
    }
    counter < numIters && subRes.reduce((x, y) => x + y) >= eps
  }

  override def call(): Array[T] = {
    var knots: Array[T] = KMeans.initializeKnots(numKnots, matrix)
    var oldKnots: Array[T] = knots
    val info = KMeans.splitComputations(matrix.m, matrix.n)

    do {

      // define tasks
      val subComputations = info.map {
        case (is, ie, js, je) => new FutureTask[Array[(T, Int)]](new AuxKMeans(matrix(is until ie toArray, js until je toArray), knots, metric))
      }

      // run tasks
      subComputations.foreach(pool.execute(_))

      // collect and 'merge' task results
      val subResults = subComputations.
        map(_.get).
        reduce(
          (arr1, arr2) => {
            for (el <- 0 until arr1.length) yield {
              f(arr1(el), arr2(el))
            }
          }.toArray)

      oldKnots = knots
      knots = 0 until subResults.length map (el => g(knots(el), subResults(el))) toArray

    } while (isResultBad(oldKnots, knots))

    knots
  }

}

/**
 * KMeans companion object
 */
object KMeans {

  // initialize knots randomly
  def initializeKnots[T](numK: Int, matrix: MatrixDense[T])(implicit ring: Ring[T], classTag: ClassTag[T]): Array[T] = {
    val rand = scala.util.Random
    def r: (Int, Int) = (rand.nextInt(matrix.m), scala.util.Random.nextInt(matrix.n))
    0 until numK map (k => {
      val temp = r
      matrix(temp._1, temp._2)
    }) toArray
  }

  // update knots (somewhat like gradient descent) ["g" stands for "sort of a gradient"] 
  def updateKnots[T](knots: Array[T], meanAndCount: Array[(T, Int)])(implicit g: (T, (T, Int)) => T, ring: Ring[T], classTag: ClassTag[T]): Array[T] = {
    0 until knots.length map (k =>
      ring.+(
        knots(k),
        g(knots(k), meanAndCount(k)))) toArray

    // g should probably do the following:
    //
    //          ring.-(
    //            meanAndCount(k)._1,
    //            knots(k)),
    //          meanAndCount(k)._2)

  }

  // return info on how to split the matrix (which must not be tabbed). Splits should not overlap
  def splitComputations(mm: Int, nn: Int): List[(Int, Int, Int, Int)] = {
    LargeConvolution.splitComputations(mm, nn, 0, 0)
  }

}