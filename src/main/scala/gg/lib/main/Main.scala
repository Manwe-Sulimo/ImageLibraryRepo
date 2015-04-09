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

    //    FirstStep.execute("C:\\Users\\Tinvention\\Desktop\\sample\\", "C:\\Users\\Tinvention\\Desktop\\sample\\2-out\\")

    //    val start2 = System.currentTimeMillis()
    //    SecondStep.execute("C:\\Users\\Tinvention\\Desktop\\sample\\", "C:\\Users\\Tinvention\\Desktop\\sample\\3-out\\")
    //    val end2 = System.currentTimeMillis()
    //    val time2 = ((end2 - start2).toDouble) / (1000 * 60)
    //    log.log(Level.INFO, "Total time for step2 (mins): " + time2)

    //    val start3 = System.currentTimeMillis()
    //    ThirdStep.execute("C:\\Users\\Tinvention\\Desktop\\sample\\3-out\\", "C:\\Users\\Tinvention\\Desktop\\sample\\4-out\\")
    //    val end3 = System.currentTimeMillis()
    //    val time3 = ((end3 - start3).toDouble) / (1000 * 60)
    //    log.log(Level.INFO, "Total time for step3 (mins): " + time3)

    val start4 = System.currentTimeMillis()
    FourthStep.execute("C:\\Users\\Tinvention\\Desktop\\sample\\4-out\\", "C:\\Users\\Tinvention\\Desktop\\sample\\5-out\\")
    val end4 = System.currentTimeMillis()
    val time4 = ((end4 - start4).toDouble) / (1000 * 60)
    log.log(Level.INFO, "Total time for step4 (mins): " + time4)
  }

}