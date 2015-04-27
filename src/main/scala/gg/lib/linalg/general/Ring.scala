package gg.lib.linalg.general

import scala.math.BigInt.int2bigInt
import gg.lib.linalg.general.Field.BigDecimalField
import gg.lib.linalg.general.Field.ComplexField
import gg.lib.linalg.general.Field.DoubleField
import gg.lib.linalg.general.Field.FloatField
import gg.lib.linalg.general2.Complex

/**
 * Trait representing the concept of Ring (user is responsible of making sure that (T,+,*) is actually a Ring)
 * Won't make a different trait for Ring with * commutative etc
 *
 * @author Manwe-Sulimo
 *
 */
trait Ring[@specialized(Int, Double, Long, Float) T] extends Rng[T] {
  def one: T
}

object Ring {

  import gg.lib.linalg.general.Field._

  // Int ring
  implicit object IntRing extends Ring[Int] {
    override def +(a: Int, b: Int): Int = a + b
    override def -(a: Int): Int = -a
    override def zero: Int = 0
    override def *(a: Int, b: Int): Int = a * b
    override def one: Int = 1
  }

  // Long ring
  implicit object LongRing extends Ring[Long] {
    override def +(a: Long, b: Long): Long = a + b
    override def -(a: Long): Long = -a
    override def zero: Long = 0
    override def *(a: Long, b: Long): Long = a * b
    override def one: Long = 1
  }

  // Bigint ring
  implicit object BigIntRing extends Ring[BigInt] {
    override def +(a: BigInt, b: BigInt): BigInt = a + b
    override def -(a: BigInt): BigInt = -a
    override def zero: BigInt = 0
    override def *(a: BigInt, b: BigInt): BigInt = a * b
    override def one: BigInt = 1
  }

  // fields -> rings
  implicit val FloatRing: Ring[Float] = FloatField
  implicit val DoubleRing: Ring[Double] = DoubleField
  implicit val BigDecimalRing: Ring[BigDecimal] = BigDecimalField
  implicit val ComplexRing: Ring[Complex] = ComplexField
}