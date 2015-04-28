package gg.lib.img

import java.awt.image.BufferedImage
import java.io.File
import scala.language.postfixOps
import gg.lib.linalg.general2.MatrixDense
import gg.lib.linalg.general2.MatrixDense._
import gg.lib.utils.SetUtils.cross
import javax.imageio.ImageIO
import scala.reflect.ClassTag
import gg.lib.linalg.general.Ring

object Images {

  /**
   * Read an image
   */
  def readImage[T](path: String, byte2entry: Int => T)(implicit ring: Ring[T], classTag: ClassTag[T]): MatrixDense[T] = {
    //read the image as a buffered image
    val file = new File(path)
    val img: BufferedImage = ImageIO.read(file)

    //parse out the array of (row,column) indexes
    val numColumns = img getWidth
    val numRows = img getHeight
    val res = zeros[T](numRows, numColumns)
    for (i <- 0 until numRows; j <- 0 until numColumns) {
      res(i, j) = byte2entry(img.getRGB(j, i))
    }
    res
  }

  /**
   * Write an image
   */
  def writeImage[T](matrix: MatrixDense[T], outPath: String, format: String, byteType: Int, entry2byte: T => Int) = {
    val width = matrix.n
    val height = matrix.m
    val out = new BufferedImage(width, height, byteType)

    for (i <- 0 until height; j <- 0 until width) {
      out.setRGB(j, i, entry2byte(matrix(i, j)))
    }

    ImageIO.write(out, format, new File(outPath))
  }

}