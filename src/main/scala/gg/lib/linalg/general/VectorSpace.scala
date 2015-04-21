package gg.lib.linalg.general

/**
 * Trait representing the concept of VectorSpace (additive notation +) (user is responsible of making sure that (T,+,*) is actually a Field and U a T-VectorSpace)
 *
 * @author Manwe-Sulimo
 *
 */
trait VectorSpace[T, @specialized(Int, Double, Long, Float) U] extends Module[T, U] {
  override val ring: Field[T]
}