package gg.lib.main

import gg.lib.linalg.DMatrix
import java.awt.image.BufferedImage
import gg.lib.linalg.Intero
import scala.io.BufferedSource
import gg.lib.img.Images
import java.util.logging.Logger
import java.io.File
import scala.io.Source
import java.util.logging.Level
import gg.lib.utils.ImgUtils
import gg.lib.linalg.DSInt3
import scala.util.Random

object KMeans extends ImgUtils {
  implicit val trasfDSInt3: ((Int, Int, Int)) => (DSInt3) = (x) => DSInt3(x)
  private val log: Logger = Logger.getGlobal()
  def execute(inDir: String, outDir: String) = {

    // read image files
    val dir = new File(inDir)
    val files = dir.listFiles().filter(el => el.getName().contains("jpeg")).map(el => el.getPath)

    // load images, parse data, save results
    files.foreach(filePath => {

      val fileName = new File(filePath).getName()
      val outFile = outDir + fileName.split('.')(0) + ".jpg"

      log.log(Level.INFO, "Starting with " + fileName)

      val (img, pixelIndexes) = read(filePath)
      val width = img.getWidth
      val height = img.getHeight

      val matrix = new DMatrix[(Int, Int, Int)](height, width, pixelIndexes.map { case (row, col) => getRGB_A(img.getRGB(col, row)) })

      val result = compute(matrix)
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
  def compute(matrix: DMatrix[(Int, Int, Int)]): DMatrix[(Int, Int, Int)] = {
    val rand=new Random
    def r=math.abs(rand.nextInt(256))
    def randomEntry=(r,r,r)
    ???
  }

  /**
   * write
   */
  def write(result: DMatrix[(Int, Int, Int)], pixelIndexes: Array[(Int, Int)], outPath: String) = {
    Images.writeImage(result, pixelIndexes, outPath, "jpeg", BufferedImage.TYPE_INT_RGB, int2RGB_A)
  }

}