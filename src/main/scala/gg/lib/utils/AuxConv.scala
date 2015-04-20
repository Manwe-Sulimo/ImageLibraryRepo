package gg.lib.utils

import java.util.concurrent.Callable
import gg.lib.linalg.DMatrix

class AuxConv(ind: Int, mat: DMatrix[Double], that: DMatrix[Double]) extends Callable[Array[Double]] {

  def call(): Array[Double] = {
    println("Starting computation of split number: " + ind)
    val res = mat.tabbedConv(that).collect
    println("Ended computation of split number: " + ind)
    res
  }
}