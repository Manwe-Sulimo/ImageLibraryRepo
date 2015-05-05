package gg.lib.utils.kmeans

import java.util.logging.Logger
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import org.scalatest.junit.JUnitRunner
import gg.lib.utils.settings.Settings
import gg.lib.linalg.general2.MatrixDense

/**
 *
 * @author Manwe-Sulimo
 *
 */

@RunWith(classOf[JUnitRunner])
class KMeansTest extends FunSuite with BeforeAndAfter with Checkers {
  private val log: Logger = Logger.getGlobal()

  // set up env
  before {
    Settings.isTest = true
  }
  // clean up env
  after {
    Settings.isTest = false
  }

  test("KMeans splitComputations should work as expected") {
    val (m1, n1) = (7, 11)
    val expected1 = List((0, 6, 0, 10))
    val (m2, n2) = (24, 40)
    val expected2 = List((0, 9, 0, 14), (0, 9, 15, 39), (10, 23, 0, 14), (10, 23, 15, 39))
    assert(KMeans.splitComputations(m1, n1) == expected1)
    assert(KMeans.splitComputations(m2, n2) == expected2)
  }

  test("KMeans initializeKnots should work as expected") {
    val els = Array(
      1, 2, 3, 0, 0, 0, 0,
      0, 0, 0, 0, 4, 0, 0,
      0, 0, 0, 0, 5, 6, 0,
      0, 0, 2, 0, 0, 0, 7,
      2, 3, 0, 0, 0, 0, 0,
      9, 9, 0, 0, 0, 0, 8)
    val mat = new MatrixDense(6, 7, els)
    val res = KMeans.initializeKnots(4, mat)
    assert(res.length == 4)
    assert(res.forall(el => els.contains(el)))
  }

  test("KMeans updateKnots should work as expected") {
    implicit val f: (Int, (Int, Int)) => Int = (x, y) => (y._1 - x) / 2
    val knots = Array(10, 100, 150)
    val mAc = Array((1, 1000), (100, 5), (155, 10))
    val expected = Array(6, 100, 152)
    assert(KMeans.updateKnots[Int](knots, mAc).zip(expected).forall(el => el._1 == el._2))
  }

}