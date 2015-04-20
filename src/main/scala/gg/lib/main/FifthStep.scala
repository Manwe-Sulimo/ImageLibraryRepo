package gg.lib.main

import java.io.PrintWriter
import scala.util.Sorting
import java.awt.image.BufferedImage
import gg.lib.linalg.Intero
import gg.lib.img.Images
import java.util.logging.Logger
import java.io.File
import java.util.logging.Level
import scala.io.BufferedSource
import scala.io.Source
import gg.lib.linalg.DMatrix
import gg.lib.utils.ImgUtils

object FifthStep extends ImgUtils {
  implicit val trasfInt: (Int) => (Intero) = x => Intero(x)
  private val log: Logger = Logger.getGlobal()
  def execute(inDir: String, statDir: String, outDir: String) = {

    def readStats(file: String): Map[Int, Int] = {
      val source: BufferedSource = Source.fromFile(file)

      val x: Iterator[Option[(Int, Int)]] = source.getLines.map(line => if (line startsWith "(") {
        val temp = line.substring(line.indexOf("(") + 1, line.indexOf(")")).split(',').map(_.toInt)
        Some((temp(0), temp(1)))
      } else None)

      x.filter(_ != None).map(_ get).toMap
    }

    // read image files
    val dir = new File(inDir)
    val files = dir.listFiles().filter(el => el.getName().contains("tif")).map(el => el.getPath)

    // load images, parse data, save results
    files.foreach(filePath => {

      val fileName = new File(filePath).getName()
      val statFile = statDir + fileName.split('.')(0) + ".stat"
      val outFile = outDir + fileName.split('.')(0) + ".tif"

      log.log(Level.INFO, "Starting with " + fileName)

      val statMap = readStats(statFile)
      val (img, pixelIndexes) = read(filePath)
      val width = img.getWidth
      val height = img.getHeight

      val matrix = new DMatrix(height, width, pixelIndexes.map { case (row, col) => img.getRGB(col, row) })

      val result = compute(matrix, statMap, 10002, 12759)
      write(result, pixelIndexes, outFile)

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
  def compute(matrix: DMatrix[Int], statMap: Map[Int, Int], threshold1: Int, threshold2: Int): DMatrix[Int] = {
    val result = matrix.map(el => {
      val temp = statMap.get(el)
      if (temp != None && parse(getRGBA(el), 5) > 5 && temp.get > threshold1 && temp.get < threshold2) 255 else 0
    })
    result
  }

  /**
   * write
   */
  def write(result: DMatrix[Int], pixelIndexes: Array[(Int, Int)], outPath: String) = {

    Images.writeImage(result, pixelIndexes, outPath, "tif", BufferedImage.TYPE_BYTE_BINARY, int2Grey)
  }

}