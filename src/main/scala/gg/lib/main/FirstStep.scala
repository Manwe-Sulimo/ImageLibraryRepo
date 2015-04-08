package gg.lib.main

import java.io.File
import java.awt.image.BufferedImage
import gg.lib.img.Images
import gg.lib.linalg.DMatrix
import gg.lib.linalg.Intero
import gg.lib.utils.ImgUtils
import java.util.logging.Logger
import java.util.logging.Level

object FirstStep extends ImgUtils {
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

      log.log(Level.INFO, "Starting blue")

      val elementsBlue = pixelIndexes.map { case (row, col) => parse(getRGBA(img.getRGB(col, row)), 0) }
      val b = new DMatrix[Int](height, width, elementsBlue)
      val bc = compute(b, height, width)
      write(bc, pixelIndexes, outDir + fileName.replaceFirst("\\.", "_blue."))

      log.log(Level.INFO, "Ended blue")
      log.log(Level.INFO, "Starting green")

      val elementsGreen = pixelIndexes.map { case (row, col) => parse(getRGBA(img.getRGB(col, row)), 1) }
      val g = new DMatrix[Int](height, width, elementsGreen)
      val gc = compute(g, height, width)
      write(gc, pixelIndexes, outDir + fileName.replaceFirst("\\.", "_green."))

      log.log(Level.INFO, "Ended green")
      log.log(Level.INFO, "Starting red")

      val elementsRed = pixelIndexes.map { case (row, col) => parse(getRGBA(img.getRGB(col, row)), 2) }
      val r = new DMatrix[Int](height, width, elementsRed)
      val rc = compute(r, height, width)
      write(rc, pixelIndexes, outDir + fileName.replaceFirst("\\.", "_red."))

      log.log(Level.INFO, "Ended  red")
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

    // filtro media 1
    val filtro0 = new DMatrix[Int](3, 3, Array(1, 1, 1,
      1, 1, 1,
      1, 1, 1))

    // filtro gradiente 1
    val filtro1 = new DMatrix[Int](3, 3, Array(-1, 0, -1,
      0, 0, 0,
      1, 0, 1))

    // filtro gradiente 2
    val filtro2 = new DMatrix[Int](3, 3, Array(0, -1, 0,
      -1, 0, 1,
      0, 1, 0))

    // filtro media 2
    val filtro3 = new DMatrix[Int](3, 3, Array(1, 1, 1,
      1, 0, 1,
      1, 1, 1))

    // media  
    val mat1 = matrix.conv(filtro0, 0).map(el => el / 9)
    // gradiente 1
    val mat2 = mat1.conv(filtro1, 0).map(el => math.abs(el))
    // gradiente 2
    val mat3 = mat1.conv(filtro2, 0).map(el => math.abs(el))
    // max gradienti (OSS: temporaneo, ci sarà max(mat1,mat2) in  versioni future di Matrix)
    val mat4 = new DMatrix(height, width, mat2.collect.zip(mat3.collect).map { case (a, b) => math.max(a, b) })

    // calcolo gradiente medio
    val meanGrad = mat4.reduce((a, b) => a + b) / (height * width)

    // binarizzazione
    val mat5 = mat4.map(el => if (el > 3 * meanGrad) 1 else 0)

    // media intorno
    //    val mat6 = mat5.conv(filtro3, 0).map(el => if (el >= 3) 1 else 0)
    //    val mat7 = mat6.conv(filtro3, 0).map(el => if (el >= 2) 1 else 0)
    //    val result = mat7.conv(filtro3, 0).map(el => if (el >= 1) 255 else 0)

    val result = mat5.map(el => if (el >= 3) 255 else 0)
    result
  }

  /**
   * write
   */
  def write(matrix: DMatrix[Int], pixelIndexes: Array[(Int, Int)], outPath: String) = {
    Images.writeImage(matrix, pixelIndexes, outPath, "jpeg", BufferedImage.TYPE_BYTE_BINARY, int2Grey)
  }

}