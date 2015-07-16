package gg.lib.utils.components

import org.junit.runner.RunWith
import org.scalatest.prop.Checkers
import org.scalatest.FunSuite
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import gg.lib.linalg.general2.MatrixDense
import java.util.logging.Logger
import org.scalatest.BeforeAndAfter
import gg.lib.utils.settings.Settings
import org.scalatest.junit.JUnitRunner
import org.scalatest.BeforeAndAfterAll

/**
 *
 * @author Manwe-Sulimo
 *
 */

@RunWith(classOf[JUnitRunner])
class ConnectedComponentsTest extends FunSuite with BeforeAndAfterAll with Checkers {
  private val log: Logger = Logger.getGlobal()

  // set up env
  override def beforeAll() {
    Settings.isTest = true
  }
  // clean up env
  override def afterAll() {
    Settings.isTest = false
  }

  test("ConnectedComponents call should work as expected in single-thread mode") {
    val els = Array(
      1, 1, 1, 0, 0, 0, 0,
      0, 0, 0, 0, 1, 0, 0,
      0, 0, 0, 0, 1, 1, 0,
      0, 0, 1, 0, 0, 0, 1,
      1, 1, 0, 0, 0, 0, 0,
      1, 1, 0, 0, 0, 0, 1)
    val expectedEls = Array(
      3, 3, 3, 0, 0, 0, 0,
      0, 0, 0, 0, 28, 0, 0,
      0, 0, 0, 0, 28, 28, 0,
      0, 0, 37, 0, 0, 0, 28,
      37, 37, 0, 0, 0, 0, 0,
      37, 37, 0, 0, 0, 0, 42)
    val mat = new MatrixDense(6, 7, els)
    val expected = new MatrixDense(6, 7, expectedEls)
    val pool: ExecutorService = Executors.newFixedThreadPool(7)
    try {
      val res = new ConnectedComponents(mat, pool).call
      assert(res == expected)
    } finally {
      pool.shutdown()
    }
  }

  test("ConnectedComponents call should work as expected in multi-thread mode") {
    val els = Array(
      1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1,
      0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
      1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1,
      0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1,
      1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1,
      1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1,
      1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1,
      0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
      0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1,
      0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1,
      1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1,
      1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1,
      1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1,
      0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1,
      0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1,
      0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1,
      1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1,
      1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1,
      1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1,
      0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
      0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1,
      0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1,
      1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1,
      1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1)
    val expectedEls = Array(
      3, 3, 3, 0, 0, 0, 0, 360, 360, 360, 360, 0, 0, 0, 360,
      0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 360, 0, 0, 360,
      31, 0, 0, 0, 100, 100, 0, 0, 0, 0, 0, 360, 360, 0, 360,
      0, 0, 93, 0, 0, 0, 100, 0, 0, 100, 0, 0, 0, 360, 360,
      93, 93, 0, 0, 0, 0, 0, 100, 100, 0, 0, 0, 0, 0, 360,
      93, 93, 0, 0, 0, 0, 100, 100, 100, 0, 0, 0, 0, 360, 360,
      93, 93, 93, 0, 0, 0, 0, 100, 100, 100, 0, 0, 0, 0, 360,
      0, 0, 0, 0, 280, 0, 0, 0, 0, 0, 0, 360, 0, 0, 360,
      0, 0, 0, 280, 280, 280, 0, 0, 0, 0, 0, 360, 360, 0, 360,
      0, 0, 280, 0, 0, 0, 280, 0, 0, 280, 0, 0, 0, 360, 360,
      280, 280, 0, 0, 0, 0, 0, 280, 280, 0, 0, 0, 0, 0, 360,
      280, 280, 0, 0, 0, 0, 280, 280, 280, 0, 0, 0, 0, 360, 360,
      280, 280, 280, 280, 0, 0, 0, 280, 280, 280, 0, 0, 0, 0, 360,
      0, 0, 0, 0, 280, 0, 0, 0, 0, 280, 0, 360, 0, 0, 360,
      0, 0, 0, 0, 280, 280, 0, 0, 0, 280, 0, 360, 360, 0, 360,
      0, 0, 273, 0, 0, 0, 280, 0, 0, 280, 0, 0, 0, 360, 360,
      273, 273, 0, 0, 0, 0, 0, 280, 280, 0, 0, 0, 0, 0, 360,
      273, 273, 0, 0, 0, 0, 280, 280, 280, 0, 0, 0, 0, 360, 360,
      273, 273, 273, 0, 0, 0, 0, 280, 280, 280, 0, 0, 0, 0, 360,
      0, 0, 0, 0, 354, 0, 0, 0, 0, 0, 0, 360, 0, 0, 360,
      0, 0, 0, 0, 354, 354, 0, 0, 0, 0, 0, 360, 360, 0, 360,
      0, 0, 347, 0, 0, 0, 354, 0, 0, 354, 0, 0, 0, 360, 360,
      347, 347, 0, 0, 0, 0, 0, 354, 354, 0, 0, 0, 0, 0, 360,
      347, 347, 0, 0, 0, 0, 354, 354, 354, 0, 0, 0, 0, 360, 360)
    val mat = new MatrixDense(24, 15, els)
    val expected = new MatrixDense(24, 15, expectedEls)
    val pool: ExecutorService = Executors.newFixedThreadPool(7)
    try {
      val res = new ConnectedComponents(mat, pool).call
      assert(res == expected)
    } finally {
      pool.shutdown()
    }
  }
}