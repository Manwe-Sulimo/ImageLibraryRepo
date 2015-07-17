package gg.lib.utils.kmeans

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.FutureTask
import scala.language.postfixOps
import scala.reflect.ClassTag
import gg.lib.linalg.general.Ring
import gg.lib.linalg.general2.MatrixDense
import gg.lib.utils.convolutions.LargeConvolution
import java.util.logging.Logger
import gg.lib.utils.settings.Settings

/**
 * KMeans for matrices
 *
 * @author Manwe-Sulimo
 *
 * TODO: input validation and error handling
 */
class KMeans[T](matrix: MatrixDense[T], numKnots: Int, numIters: Int, eps: Double, metric: (T, T) => Double, pool: ExecutorService)(implicit f: ((T, Int), (T, Int)) => (T, Int), g: (T, (T, Int)) => T, ring: Ring[T], h: (T, Int) => T, classTag: ClassTag[T]) extends Callable[Array[T]] {
  private val log = Logger.getGlobal()

  private var counter = 0
  private var sk: Array[T] = Array.empty[T]
  def _sk = sk

  def this(matrix: MatrixDense[T], startingKnots: Array[T], numIters: Int, eps: Double, metric: (T, T) => Double, pool: ExecutorService)(implicit f: ((T, Int), (T, Int)) => (T, Int), g: (T, (T, Int)) => T, h: (T, Int) => T, ring: Ring[T], classTag: ClassTag[T]) = {
    this(matrix, startingKnots.length, numIters, eps, metric, pool)
    sk = startingKnots
  }

  //--------------------------------------
  // validate input through requirements
  //--------------------------------------
  // number of knots should at least be 1
  require(numKnots > 0)
  // number of iterations should at least be 1
  require(numIters > 0)
  // tolerance should be strictly greater than 0
  require(eps > 0)
  //--------------------------------------

  // determine whether the result is good enough (error<eps || counter >= numIters) or not
  private def isResultBad(arr1: Array[T], arr2: Array[T]): Boolean = {
    counter = counter + 1
    val subRes = for (i <- 0 until arr1.length) yield {
      metric(arr1(i), arr2(i))
    }
    counter < numIters && subRes.reduce((x, y) => x + y) >= eps
  }

  override def call(): Array[T] = {
    if (sk.length == 0) {
      sk = KMeans.initializeKnots(numKnots, matrix)
    }
    var knots: Array[T] = sk
    var oldKnots: Array[T] = knots
    val info = KMeans.splitComputations(matrix.m, matrix.n)

    do {

      // define tasks
      val subComputations = info.map {
        case (is, ie, js, je) => new FutureTask[Array[(T, Int)]](new AuxKMeans(matrix(is to ie toArray, js to je toArray), knots, metric))
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
          }.toArray).
          map(el => (h(el._1, el._2), el._2))

      oldKnots = knots
      knots = KMeans.updateKnots(knots, subResults)

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
    val knots: Array[Option[T]] = Array.fill(numK)(None)
    def r: (Int, Int) = (rand.nextInt(matrix.m), scala.util.Random.nextInt(matrix.n))
    var counter = 0;
    var numLoop = 0;
    while (counter < numK) {
      numLoop += 1
      val randomIndexes = r
      val randomElement = matrix(randomIndexes._1, randomIndexes._2)
      if (!(knots contains Some(randomElement)) || numLoop > Settings.maxLoops) {
        knots(counter) = Some(randomElement)
        counter += 1
      }
    }
    knots.map(_.get)
  }

  // update knots
  // "g" stands for "get some magic done" and should probably do the following: ?./(ring.-(meanAndCount(k)._1, knots(k)),meanAndCount(k)._2))
  def updateKnots[T](knots: Array[T], meanAndCount: Array[(T, Int)])(implicit g: (T, (T, Int)) => T, ring: Ring[T], classTag: ClassTag[T]): Array[T] = {
    0 until knots.length map (k =>
      ring.+(
        knots(k),
        g(knots(k), meanAndCount(k)))) toArray
  }

  // return info on how to split the matrix (which must not be tabbed). Splits should not overlap
  def splitComputations(mm: Int, nn: Int): List[(Int, Int, Int, Int)] = {
    LargeConvolution.splitComputations(mm, nn, 0, 0)
  }

}