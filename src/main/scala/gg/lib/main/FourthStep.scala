package gg.lib.main

import java.awt.image.BufferedImage
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger
import gg.lib.img.Images
import gg.lib.linalg.DMatrix
import gg.lib.linalg.Intero
import gg.lib.utils.ImgUtils
import scala.util.Sorting
import java.io.PrintWriter

object FourthStep extends ImgUtils {
  implicit val trasfInt: (Int) => (Intero) = x => Intero(x)
  private val log: Logger = Logger.getGlobal()
  def execute(inDir: String, outDir: String) = {
    // read image files
    val dir = new File(inDir)
    val files = dir.listFiles().filter(el => el.getName().contains("jpeg")).map(el => el.getPath)

    // load images, parse data, save results
    files.foreach(filePath => {

      val fileName = new File(filePath).getName()
      val outFile = outDir + fileName.split('.')(0) + ".stat"

      log.log(Level.INFO, "Starting with " + fileName)

      val (img, pixelIndexes) = read(filePath)
      val width = img.getWidth
      val height = img.getHeight

      val elements = pixelIndexes.map { case (row, col) => ((row, col), img.getRGB(col, row)) }

      val result = compute(elements)

      write(result, outFile)

      log.log(Level.INFO, "Ended with " + outFile)
    })
  }

  /**
   * read
   */
  def read(filePath: String): (BufferedImage, Array[(Int, Int)]) = {
    Images.readImage(filePath)
  }

  /**
   * compute
   */
  def compute(elements: Array[((Int, Int), Int)]): Array[(Int, Int)] = {
    val result = elements.groupBy(el => el._2).map(el => (el._1, el._2.length)).toArray

    class F extends Ordering[(Int, Int)] {
      override def compare(x: (Int, Int), y: (Int, Int)) = x._2 - y._2
    }

    Sorting.quickSort[(Int, Int)](result)(new F)

    result
  }

  /**
   * write
   */
  def write(result: Array[(Int, Int)], outPath: String) = {
    val pw = new PrintWriter(outPath)

    val totals = result.map(_._2).reduce((a, b) => a + b)

    result.foreach(el => pw.write(el.toString + "\n"))
    pw.write("Totals: " + totals)
    pw.close()
  }

}