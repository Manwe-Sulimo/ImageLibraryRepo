package gg.lib.linalg.general

/**
 * Trait representing the concept of Monoid (user is responsible of making sure that (T,+) is actually a Monoid)
 * Won't make a different trait for Monoid with + commutative
 *
 * Using additive notation for convenience
 * 
 * @author Manwe-Sulimo
 *
 */
trait Monoid[@specialized(Int, Double, Long, Float) T] extends Semigroup[T] {
  def zero: T
}