package gg.lib.main

import gg.lib.linalg.DMatrix
import java.awt.image.BufferedImage
import gg.lib.linalg.Intero
import gg.lib.img.Images
import java.util.logging.Logger
import java.io.File
import java.util.logging.Level
import gg.lib.utils.ImgUtils

object SecondStep extends ImgUtils {
  implicit val trasfInt: (Int) => (Intero) = x => Intero(x)
  private val log: Logger = Logger.getGlobal()

  def execute(inDir: String, outDir: String) = {
    // read image files
    val dir = new File(inDir)
    val files = dir.listFiles().filter(el => el.getName().contains("jpeg")).map(el => el.getPath)

    // load images, parse data, save results
    files.foreach(filePath => {

      val fileName = new File(filePath).getName()

      val (img, pixelIndexes) = read(filePath)
      val width = img.getWidth
      val height = img.getHeight

      log.log(Level.INFO, "Starting white")

      val elementsWhite = pixelIndexes.map { case (row, col) => parse(getRGBA(img.getRGB(col, row)), 4) }
      val w = new DMatrix[Int](height, width, elementsWhite)
      val wc = compute(w, height, width)
      write(wc, pixelIndexes, outDir + fileName.replaceFirst("\\.", "_white."))

      log.log(Level.INFO, "Ended  white")
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
  def compute(matrix: DMatrix[Int], height: Int, width: Int) = {

    // filtro 0
    val filtro0 = new DMatrix[Int](3, 3, Array(1, 1, 1,
      1, 0, 1,
      1, 1, 1))

    // filtro 1
    val filtro1 = new DMatrix[Int](3, 3, Array(1, 0, 0,
      0, 1, 0,
      0, 0, 1))

    // filtro 2
    val filtro2 = new DMatrix[Int](3, 3, Array(0, 1, 0,
      0, 0, 0,
      0, 1, 0))

    // filtro 3
    val filtro3 = new DMatrix[Int](3, 3, Array(0, 0, 1,
      0, 0, 0,
      1, 0, 0))

    // filtro 4
    val filtro4 = new DMatrix[Int](3, 3, Array(0, 0, 0,
      1, 0, 1,
      0, 0, 0))

    // soglia intorno  
    val mat0 = matrix.conv(filtro0, 0).map(el => if (el >= 2) 1 else 0)

    // applicazione filtro
    val mat1 = mat0.conv(filtro1, 0).map(el => if (el >= 2) 1 else 0)
    // applicazione filtro
    val mat2 = mat0.conv(filtro2, 0).map(el => if (el >= 2) 1 else 0)
    // applicazione filtro
    val mat3 = mat0.conv(filtro3, 0).map(el => if (el >= 2) 1 else 0)
    // applicazione filtro
    val mat4 = mat0.conv(filtro4, 0).map(el => if (el >= 2) 1 else 0)

    // somma risultati
    val mat = (mat1 + mat2 + mat3 + mat4).map(el => if (el > 0) 1 else 0)

    // applicazione filtro
    val mat5 = mat.conv(filtro1, 0).map(el => if (el >= 2) 1 else 0)
    // applicazione filtro
    val mat6 = mat.conv(filtro2, 0).map(el => if (el >= 2) 1 else 0)
    // applicazione filtro
    val mat7 = mat.conv(filtro3, 0).map(el => if (el >= 2) 1 else 0)
    // applicazione filtro
    val mat8 = mat.conv(filtro4, 0).map(el => if (el >= 2) 1 else 0)
    
    val result = mat.map(el => if (el > 0) 255 else 0)
    result
  }

  /**
   * write
   */
  def write(matrix: DMatrix[Int], pixelIndexes: Array[(Int, Int)], outPath: String) = {
    Images.writeImage(matrix, pixelIndexes, outPath, "jpeg", BufferedImage.TYPE_BYTE_BINARY, int2Grey)
  }

}