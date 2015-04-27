//package gg.lib.utils
//
//import java.util.concurrent.Callable
//import gg.lib.linalg.generalClasses.MatrixDense
//
//class AuxConv(ind: Int, mat: MatrixDense[Double], that: MatrixDense[Double]) extends Callable[Array[Double]] {
//
//  def call(): Array[Double] = {
//    println("Starting computation of split number: " + ind)
//    val res = mat.tabbedConv(that).collect
//    println("Ended computation of split number: " + ind)
//    res
//  }
//}