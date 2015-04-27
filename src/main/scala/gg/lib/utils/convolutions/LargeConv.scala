//package gg.lib.utils
//
//import java.util.concurrent.Callable
//import gg.lib.linalg.DMatrix
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//import java.util.concurrent.FutureTask
//import gg.lib.linalg.Decimale
//import java.util.logging.Logger
//
//class LargeConv(matrix: DMatrix[Double], that: DMatrix[Double], tabValue: Double, pool: ExecutorService) extends Callable[DMatrix[Double]] {
//  implicit val f: Double => Decimale = x => Decimale(x)
//  private val log: Logger = Logger.getGlobal()
//
//  //magic thread threshold
//  val maxThreads = 7
//  val threshold: Int = 300000 //TODO
//  val rt: Int = 100 //TODO
//  val ct: Int = 100 //TODO
//
//  val (sm, sn) = matrix.size
//  val (q, r) = ((that.size._1 - 1) / 2, (that.size._2 - 1) / 2)
//  val tabbed = matrix.tab(q, r, tabValue)
//  val (m, n) = tabbed.size
//
//  def call(): DMatrix[Double] = {
//
//    val info = splitsInfo
//
//    val subComputations = info.map {
//      case (ind, (start, end)) => new FutureTask[Array[Double]](new AuxConv(ind, tabbed.subMatrix(start to end toArray, 0 until n toArray), that))
//    }
//
//    subComputations.foreach(pool.execute(_))
//
//    val t = Array.empty[Double]
//
//    val resultingEls = subComputations.map(_.get).foldLeft[Array[Double]](Array.empty[Double])((a, b) => a ++ (b))
//
//    new DMatrix(sm, sn, resultingEls)
//
//  }
//
//  /**
//   * returns info on how to split the already tabbed matrix
//   */
//  def splitsInfo: List[(Int, (Int, Int))] = {
//    //if matrix is too big, split it (splits must intersect adjacent splits)
//    //current implementation splits only by rows
//    //eventually implement splitting by row and columns
//    var rowSplits = if (m * n >= threshold) m / rt + 1 else 1
//    var step = m / rowSplits
//
//    //TODO change magic resizing to something more intelligent
//    if (step < 2 * q) {
//      step = 2 * q
//    }
//
//    var nodes = ((0 to m by step).toList).dropRight(1)
//    val lastIndex = nodes.length - 1
//
//    //map the split index to the indexes of the first and last row (both inclusive) which should be included in that split
//    nodes.zipWithIndex.map(el => el._2 match {
//      case o if (lastIndex == o) => o -> (math.max(nodes(o) - q, 0), m - 1)
//      case 0 => 0 -> (nodes(0), nodes(1) + q - 1)
//      case o => o -> (nodes(o) - q, nodes(o + 1) + q - 1)
//    })
//  }
//}