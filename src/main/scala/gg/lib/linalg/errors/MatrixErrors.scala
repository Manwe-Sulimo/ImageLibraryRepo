package gg.lib.linalg.errors

class IncompatibleMatrixDimensionsException(message: String = null, cause: Throwable = null) extends Exception(message, cause) {
  def this(size1: Product, size2: Product) = {
    this("The requested operation is not applicable between matrices of size " + size1 + " and matrices of size " + size2)
  }
}

class IndexIncompatibleWithMatrixDimensionException(message: String = null, cause: Throwable = null) extends Exception(message, cause)

/**
 * Use for matrices with incompatible types, not for matrices of the same type but with incompatible parameter types
 */
class IncompatibleMatrixTypeException[A, B](message: String = null, cause: Throwable = null) extends Exception(message, cause) {
  def this(type1: Class[A], type2: Class[B]) = {
    this("A matrix of type " + type1.getName() + " is not compatible with a matrix of type " + type2.getName())
  }
}
