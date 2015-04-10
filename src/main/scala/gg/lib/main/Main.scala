package gg.lib.main

import java.util.logging.Level
import java.util.logging.Logger
import scala.language.postfixOps
import gg.lib.utils.SetUtils
import gg.lib.linalg.DMatrix
import gg.lib.linalg.Intero
import gg.lib.linalg.DSInt3
import scala.reflect.ClassTag
import scala.reflect._
import gg.lib.linalg.Ring
import scala.util.Random
object Prova {
  private val log = Logger.getGlobal()

  implicit val f: ((Int, Int, Int)) => (DSInt3) = (x) => DSInt3(x)
  implicit val tag: ClassTag[(Int, Int, Int)] = classTag[(Int, Int, Int)]

  def main(args: Array[String]): Unit = {
val rand=new Random
    def r=math.abs(rand.nextInt(256))
    def randomEntry=(r,r,r)
    for(i<-1 to 10)
    println(randomEntry)
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

    //    val start4 = System.currentTimeMillis()
    //    FourthStep.execute("C:\\Users\\Tinvention\\Desktop\\sample\\4-out\\", "C:\\Users\\Tinvention\\Desktop\\sample\\5-out\\")
    //    val end4 = System.currentTimeMillis()
    //    val time4 = ((end4 - start4).toDouble) / (1000 * 60)
    //    log.log(Level.INFO, "Total time for step4 (mins): " + time4)

    //    val start5 = System.currentTimeMillis()
    //    FifthStep.execute("C:\\Users\\Tinvention\\Desktop\\sample\\4-out\\", "C:\\Users\\Tinvention\\Desktop\\sample\\5-out\\", "C:\\Users\\Tinvention\\Desktop\\sample\\6-out\\")
    //    val end5 = System.currentTimeMillis()
    //    val time5 = ((end5 - start5).toDouble) / (1000 * 60)
    //    log.log(Level.INFO, "Total time for step5 (mins): " + time5)
  }

}