package gg.lib.linalg.general

/**
 * Trait representing the concept of Module (additive notation +) over a Ring (user is responsible of making sure that (T,+,*) is actually a Ring and U a T-module)
 * Assume this is a bilateral unitaty Module (user should make sure of this)
 *
 * @author Manwe-Sulimo
 *
 */
trait Module[T, @specialized(Int, Double, Long, Float) U] extends Group[U] {
  val ring: Ring[T]
  def **(t: T, a: U): U

  //this should be a bilateral unitary module satisfying the following:
  //  def **(a: U, t: T): U = **(t, a)
}