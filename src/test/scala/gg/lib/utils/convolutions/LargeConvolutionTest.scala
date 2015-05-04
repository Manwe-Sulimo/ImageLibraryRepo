package gg.lib.utils.convolutions

import org.junit.runner.RunWith
import org.scalatest.prop.Checkers
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.logging.Logger
import org.scalatest.BeforeAndAfter
import gg.lib.utils.settings.Settings

/**
 *
 * @author Manwe-Sulimo
 *
 */

@RunWith(classOf[JUnitRunner])
class LargeConvolutionTest extends FunSuite with BeforeAndAfter with Checkers {
  private val log: Logger = Logger.getGlobal()

  // set up env
  before {
    Settings.isTest = true
  }
  // clean up env
  after {
    Settings.isTest = false
  }

  test("LargeConvolution splitComputations should work as expected") {
    import gg.lib.utils.convolutions.LargeConvolution._
    val (m1, n1, q1, r1) = (7, 11, 3, 4)
    val expected1 = List((0, 6, 0, 10))
    val (m2, n2, q2, r2) = (24, 40, 5, 3)
    val expected2 = List((0, 14, 0, 17), (0, 14, 12, 39), (5, 23, 0, 17), (5, 23, 12, 39))
    assert(splitComputations(m1, n1, q1, r1) == expected1)
    assert(splitComputations(m2, n2, q2, r2) == expected2)
  }

  test("LargeConvolution call should work as expected") {
    import gg.lib.linalg.general2.MatrixDense._
    val mat1 = diag(14, 34, 1)
    val mat2 = diag[Int](14, 34, 3); mat2(0, 0) = 2; mat2(13, 13) = 2
    val dat = diag(3, 3, 1)
    val pool: ExecutorService = Executors.newFixedThreadPool(7)
    try {
      val res = new LargeConvolution(mat1, dat, true, Some(0), pool).call
      assert(res == mat2)
    } finally {
      pool.shutdown()
    }
  }

  test("LargeConvolution call should work as expected with non square matrices") {
    import gg.lib.linalg.general2.MatrixDense._
    import gg.lib.utils.convolutions.MatrixUtils._
    val mat1 = diag(14, 34, 1)
    val mat2 = zeros[Int](14, 34)
    mat2(1, 0) = 2
    for (i <- 2 to 13) {
      mat2(i, i - 1) = 3
    }
    val dat = diag(5, 3, 1)
    val pool: ExecutorService = Executors.newFixedThreadPool(7)
    try {
      val res = new LargeConvolution(mat1, dat, true, Some(0), pool).call
      assert(res == mat2)
    } finally {
      pool.shutdown()
    }
  }

}