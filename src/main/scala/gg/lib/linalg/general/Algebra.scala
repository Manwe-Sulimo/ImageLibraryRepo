package gg.lib.linalg.general

/**
 * Trait representing the concept of Algebra (user is responsible of making sure that (T,+,*) is actually a Ring and U a T-algebra)
 *
 * @author Manwe-Sulimo
 *
 */
trait Algebra[T <: Ring[T], @specialized(Int, Double, Long, Float) U] extends Module[T, U] with Ring[U] {}