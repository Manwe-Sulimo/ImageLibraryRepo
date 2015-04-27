package gg.lib.linalg.general

import gg.lib.linalg.general.Ring.BigDecimalRing
import gg.lib.linalg.general.Ring.BigIntRing
import gg.lib.linalg.general.Ring.ComplexRing
import gg.lib.linalg.general.Ring.DoubleRing
import gg.lib.linalg.general.Ring.FloatRing
import gg.lib.linalg.general.Ring.IntRing
import gg.lib.linalg.general.Ring.LongRing
import gg.lib.linalg.general2.Complex


/**
 * Trait representing the concept of Rng (user is responsible of making sure that (T,+,*) is actually a Rng)
 * Won't make a different trait for Rng with * commutative etc
 *
 * @author Manwe-Sulimo
 *
 */
trait Rng[@specialized(Int, Double, Long, Float) T] extends Group[T] {
  def *(a: T, b: T): T
}

object Rng {

  import gg.lib.linalg.general.Ring._

  // ring -> rng
  implicit val IntRng: Rng[Int] = IntRing
  implicit val LongRng: Rng[Long] = LongRing
  implicit val BigIntRng: Rng[BigInt] = BigIntRing
  implicit val FloatRng: Rng[Float] = FloatRing
  implicit val DoubleRng: Rng[Double] = DoubleRing
  implicit val BigDecimalRng: Rng[BigDecimal] = BigDecimalRing
  implicit val ComplexRng: Rng[Complex] = ComplexRing
}