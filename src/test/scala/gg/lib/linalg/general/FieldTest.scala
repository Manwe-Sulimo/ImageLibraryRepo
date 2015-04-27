package gg.lib.linalg.general

import org.scalacheck._
import org.scalatest._
import org.scalatest.junit._
import org.scalatest.prop._
import org.junit.runner.RunWith
import gg.lib.linalg.general2.Complex

/**
 *
 * @author Manwe-Sulimo
 *
 */

@RunWith(classOf[JUnitRunner])
class FieldTest extends FunSuite with Checkers {

  test("Field[Complex] works as expected") {
    val f = gg.lib.linalg.general.Field.ComplexField
    import f._
    import Complex._
    val z: Complex = zero
    val o: Complex = one
    val a: Complex = 2.0 + 3.0 * i
    val b: Complex = 5.0 + 4.0 * i

    assert(a == Complex(2, 3))
    assert(*(z, o) == z)
    assert(f.+(z, o) == o)
    assert(/(o, o) == one)
    assert(f.-(b, a) == Complex(3.0, 1.0))
    assert(/(z, o) == z)
    assert(
      try {
        /(o, z)
        false
      } catch {
        case e: ArithmeticException => true
      })

  }

}
