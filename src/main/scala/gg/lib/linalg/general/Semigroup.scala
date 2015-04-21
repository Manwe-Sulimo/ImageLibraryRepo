package gg.lib.linalg.general

/**
 * Trait representing the concept of Semigroup (user is responsible of making sure that + is associative)
 * Won't make a different trait for Semigroup with + commutative
 * 
 * Using additive notation for convenience
 * 
 * @author Manwe-Sulimo
 * 
 */
trait Semigroup[@specialized(Int, Double, Long, Float) T] extends Serializable {
  def +(a: T, b: T): T
}