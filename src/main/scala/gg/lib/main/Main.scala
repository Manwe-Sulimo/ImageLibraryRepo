package gg.lib.main

import java.util.logging.Level
import java.util.logging.Logger
import scala.language.postfixOps
import gg.lib.utils.SetUtils
import gg.lib.linalg.DMatrix
import gg.lib.linalg.Intero

object Prova extends SetUtils {
  private val log = Logger.getGlobal()

  def main(args: Array[String]): Unit = {

    val start = System.currentTimeMillis()

    //    FirstStep.execute("C:\\Users\\Tinvention\\Desktop\\sample\\", "C:\\Users\\Tinvention\\Desktop\\sample\\out2\\")
    //    SecondStep.execute("C:\\Users\\Tinvention\\Desktop\\sample\\", "C:\\Users\\Tinvention\\Desktop\\sample\\out3\\")

    ThirdStep.execute("C:\\Users\\Tinvention\\Desktop\\sample\\out3\\", "C:\\Users\\Tinvention\\Desktop\\sample\\out4\\")

    //    implicit val trasfInt: (Int) => (Intero) = x => Intero(x)
    //
    //    val x = new DMatrix[Int](12, 11, Array(1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0,
    //      1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
    //      1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1,
    //      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    //      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    //      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    //      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    //      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    //      0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1,
    //      0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0,
    //      0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0,
    //      0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1))
    //
    //    val components = ThirdStep.compute(x)
    //
    //    for (i <- 0 until components.size._1) {
    //      println(components.row(i).collect.toList.mkString("[", "\t", "]"))
    //    }
    //    // val totals = components.map(_._2).reduce((a, b) => a + b)
    //
    //    //components.foreach(el => println(el.toString))
    //    //println("Totals: " + totals)

    val end = System.currentTimeMillis()

    val time = ((end - start).toDouble) / (1000 * 60)
    log.log(Level.INFO, "Total time (mins): " + time)
  }

}