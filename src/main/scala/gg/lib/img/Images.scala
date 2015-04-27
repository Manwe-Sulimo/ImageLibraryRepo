package gg.lib.img

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.File
import gg.lib.utils.SetUtils
import gg.lib.linalg.DMatrix
import gg.lib.utils.ImgUtils
import scala.language.postfixOps


object Images extends SetUtils {

  /**
   * Read an image
   */
  def readImage(path: String): (BufferedImage, Array[(Int, Int)]) = {
    //read the image as a buffered image
    val file = new File(path)
    val img: BufferedImage = ImageIO.read(file)

    //parse out the array of (row,column) indexes
    val columns = 0 until (img getWidth) toArray
    val rows = 0 until (img getHeight) toArray
    val pixelIndexes = cross(rows, columns)

    (img, pixelIndexes)
  }

  /**
   * Write an image
   */
  def writeImage[T](matrix: DMatrix[T], pixelIndexes: Array[(Int, Int)], outPath: String, format: String, byteType: Int, entry2byte: T => Int) = {
    val width = matrix.size._2
    val height = matrix.size._1

    val out = new BufferedImage(width, height, byteType)
    pixelIndexes.foreach { case (r, c) => out.setRGB(c, r, entry2byte(matrix(r, c))) }

    ImageIO.write(out, format, new File(outPath))
  }

}