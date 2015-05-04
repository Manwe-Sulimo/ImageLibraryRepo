package gg.lib.main

import java.util.logging.Logger
import scala.language.postfixOps
import gg.lib.main.steps.Step01
import gg.lib.utils.ImgUtils._
import java.util.logging.Level
import gg.lib.main.steps.Step02
import gg.lib.main.steps.Step03
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

object Prova {
  private val log = Logger.getGlobal()

  def main(args: Array[String]): Unit = {

    val start = System.currentTimeMillis()
    //    new Step01().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_blue", RGBA2DoubleBlue)
    //    new Step01().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_green", RGBA2DoubleGreen)
    //    new Step01().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_red", RGBA2DoubleRed)

    //    new Step02().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_blue", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_blue_2", RGBA2DoubleBlue)
    //    new Step02().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_green", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_green_2", RGBA2DoubleGreen)
    //    new Step02().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_red", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_red_2", RGBA2DoubleRed)

    //    new Step03().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_blue_2", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_blue_3", RGBA2IntBlue)
    //    new Step03().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_green_2", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_green_3", RGBA2IntGreen)
        new Step03().run("C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_red_2", "C:\\Users\\Tinvention\\Desktop\\sample\\0-processed\\temp\\test_red_3", RGBA2IntRed)


    val end = System.currentTimeMillis()
    val time = ((end - start).toDouble) / (1000 * 60)
    log.log(Level.INFO, "Total time for application (mins): " + time)

  }

}












