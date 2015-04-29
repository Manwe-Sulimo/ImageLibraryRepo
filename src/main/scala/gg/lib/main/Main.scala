package gg.lib.main

import java.util.logging.Logger
import scala.language.postfixOps
import gg.lib.main.steps.Step01
import gg.lib.utils.ImgUtils.RGBA2DoubleBlue
import gg.lib.utils.ImgUtils.RGBA2DoubleGreen
import gg.lib.utils.ImgUtils.RGBA2DoubleRed
import java.util.logging.Level
import gg.lib.main.steps.Step02

object Prova {
  private val log = Logger.getGlobal()

  def main(args: Array[String]): Unit = {

    val start = System.currentTimeMillis()
    //    new Step01().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_blue", RGBA2DoubleBlue)
    //    new Step01().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_green", RGBA2DoubleGreen)
    //    new Step01().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_red", RGBA2DoubleRed)

    new Step02().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_blue", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_blue_2", RGBA2DoubleBlue)
    new Step02().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_green", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_green_2", RGBA2DoubleGreen)
    new Step02().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_red", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_red_2", RGBA2DoubleRed)

    val end = System.currentTimeMillis()
    val time = ((end - start).toDouble) / (1000 * 60)
    log.log(Level.INFO, "Total time for application (mins): " + time)

  }

  //  def test = {
  //    //    for testing
  //    val arr = Array[Double](1, 1, 1, 1, 0, 1, 1, 1, 1)
  //
  //    val arr2 = Array[Double](
  //      0, 0, 3, 0,
  //      1, 0, 0, 4,
  //      2, 0, 0, 5,
  //      0, 0, 0, 1)
  //
  //    val x = new DMatrix[Double](4, 4, arr2)
  //    val y = new DMatrix[Double](3, 3, arr)
  //    //    x.tabbedConv(y).collect.foreach(println)
  //    println()
  //  }
}












