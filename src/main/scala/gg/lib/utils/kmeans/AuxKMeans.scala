package gg.lib.utils.kmeans

import gg.lib.linalg.general2.MatrixDense
import java.util.concurrent.Callable
import scala.reflect.ClassTag
import gg.lib.linalg.general.Ring

class AuxKMeans[T](matrix: MatrixDense[T], knots: Array[T], metric: (T, T) => Double)(implicit ring: Ring[T], classTag: ClassTag[T]) extends Callable[Array[(T, Int)]] {
  override def call(): Array[(T, Int)] = {
    ???
  }
}