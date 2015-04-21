package gg.lib.linalg.general

/**
 * Trait representing the concept of Group (user is responsible of making sure that (T,+) is actually a Group)
 * Won't make a different trait for Abelian Groups
 *
 * Using additive notation for convenience
 *
 * @author Manwe-Sulimo
 *
 */
trait Group[@specialized(Int, Double, Long, Float) T] extends Monoid[T] {
  //additive inverse or an element
  def -(a: T): T

  //convenience method for a + (-b)
  def -(a: T, b: T): T = this.+(a, this.-(b))
}