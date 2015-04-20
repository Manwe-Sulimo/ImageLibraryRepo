package gg.lib.main

import java.awt.image.BufferedImage
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger
import scala.io.Source
import gg.lib.img.Images
import gg.lib.linalg.DMatrix
import gg.lib.linalg.Intero
import gg.lib.utils.ImgUtils
import gg.lib.utils.SetUtils
import java.nio.charset.Charset
import scala.io.Codec
import java.io.PrintWriter

object ThirdStep extends ImgUtils with SetUtils {
  implicit val trasfInt: (Int) => (Intero) = x => Intero(x)
  private val log: Logger = Logger.getGlobal()

  def execute(inDir: String, outDir: String) = {
    // read image files
    val dir = new File(inDir)
    val files = dir.listFiles().filter(el => el.getName().contains("tif")).map(el => el.getPath)

    // load images, parse data, save results
    files.foreach(filePath => {

      val fileName = new File(filePath).getName()
      val outFile = outDir + fileName //.split('.')(0) + ".stats"

      val (img, pixelIndexes) = read(filePath)
      val width = img.getWidth
      val height = img.getHeight

      log.log(Level.INFO, "Starting with " + fileName)

      val elementsWhite = pixelIndexes.map { case (row, col) => parse(getRGBA(img.getRGB(col, row)), 4) }
      val w = new DMatrix[Int](height, width, elementsWhite)
      val wc = compute(w)

      write(wc, pixelIndexes, outFile)

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
  def compute(matrix: DMatrix[Int]) = {
    connectedComponents(matrix)
  }

  /**
   * write
   */
  def write(matrix: DMatrix[Int], pixelIndexes: Array[(Int, Int)], outPath: String) = {
    def f: Int => Int = x => x
    Images.writeImage(matrix, pixelIndexes, outPath, "png", BufferedImage.TYPE_INT_BGR, f)

    //    val pw = new PrintWriter(outPath)
    //
    //    val totals = components.map(_._2).reduce((a, b) => a + b)
    //
    //    components.foreach(el => pw.write(el.toString + "\n"))
    //    pw.write("Totals: " + totals)
    //    pw.close()
  }

}