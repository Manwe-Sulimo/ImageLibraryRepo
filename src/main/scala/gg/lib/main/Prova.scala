package gg.lib.main

import java.awt._
import java.awt.image.BufferedImage
import java.io._
import javax.imageio.ImageIO
import scala.sys.process._
import scala.language.postfixOps
import gg.lib.linalg.DMatrix
import gg.lib.linalg.Ring
import gg.lib.linalg.Intero
import gg.lib.linalg.errors.IncompatibleMatrixTypeException
import gg.lib.linalg.Decimale

object Prova {

  implicit val trasfInt: (Int) => (Intero) = x => Intero(x)
  implicit val trasDouble: (Double) => (Decimale) = x => Decimale(x)

  def main(args: Array[String]): Unit = {

    //    val elements = Array(0, 1, 2, 3, 10, 11, 12, 13, 20, 21, 22, 23, 30, 31, 32, 33).map(el => 1);
    //    val z = new DMatrix(4, 4, elements)
    //
    //    //    (0 to 3).map(z.row(_)).foreach(el => {
    //    //      println(el.collect.mkString("[", ",", "]"))
    //    //    })
    //
    //    //    val is = (0 until 1).toArray
    //    //    val js = (0 to 3).toArray
    //
    //    val dat = new DMatrix(3, 3, Array(0, -1, 0, -1, 0, 1, 0, 1, 0))
    //
    //    val res = z.conv(dat, 0)
    //
    //    println(res.size)
    //    (0 to 3).map(res.row(_)).foreach(el => {
    //      println(el.collect.mkString("[", ",", "]"))
    //    })

    val dir = new File("C:\\Users\\Tinvention\\Desktop\\sample")
    val files = dir.listFiles().filter(el => el.getName().contains("jpeg")).map(el => el.getPath)
    //readImg(files(0))
    files.foreach(readImg)
  }

  def readImg(path: String) = {
    //reads the image as a buffered image
    val file = new File(path)
    val img: BufferedImage = ImageIO.read(file)

    //converts the bufferedImage as an array of 'rgba pixels'
    val width = img getWidth
    val height = img getHeight
    val columns = 0 until width toArray
    val rows = 0 until height toArray

    val pixelIndexes = cross(rows, columns)

    val elementsBlue = pixelIndexes.map {
      case (r, c) => parse(getRGBA(img.getRGB(c, r)), 0)
    }
    val elementsGreen = pixelIndexes.map {
      case (r, c) => parse(getRGBA(img.getRGB(c, r)), 1)
    }
    val elementsRed = pixelIndexes.map {
      case (r, c) => parse(getRGBA(img.getRGB(c, r)), 2)
    }

    //filtri gradiente
    val filtro1 = new DMatrix[Int](3, 3, Array(-1, 0, -1,
      0, 0, 0,
      1, 0, 1))
    val filtro2 = new DMatrix[Int](3, 3, Array(0, -1, 0,
      -1, 0, 1,
      0, 1, 0))

    //filtro media
    val filtro5 = new DMatrix[Int](3, 3, Array(1, 1, 1,
      1, 1, 1,
      1, 1, 1))

    val b = new DMatrix[Int](height, width, elementsBlue)
    val g = new DMatrix[Int](height, width, elementsGreen)
    val r = new DMatrix[Int](height, width, elementsRed)

    elab(height: Int, width: Int, b: DMatrix[Int], filtro1: DMatrix[Int], filtro2: DMatrix[Int], filtro5: DMatrix[Int], file: File, pixelIndexes: Array[(Int, Int)], "b")
    elab(height: Int, width: Int, g: DMatrix[Int], filtro1: DMatrix[Int], filtro2: DMatrix[Int], filtro5: DMatrix[Int], file: File, pixelIndexes: Array[(Int, Int)], "g")
    elab(height: Int, width: Int, r: DMatrix[Int], filtro1: DMatrix[Int], filtro2: DMatrix[Int], filtro5: DMatrix[Int], file: File, pixelIndexes: Array[(Int, Int)], "r")
  }

  /*
   * Utils
   */

  def elab(height: Int, width: Int, matrix: DMatrix[Int], filtro1: DMatrix[Int], filtro2: DMatrix[Int], filtro5: DMatrix[Int], file: File, pixelIndexes: Array[(Int, Int)], colore: String) = {

    val mat1 = matrix.conv(filtro5, 0).map(el => el / 9)
    //gradiente
    val mat3 = mat1.conv(filtro1, 0).map(el => math.abs(el))
    val mat4 = mat1.conv(filtro2, 0).map(el => math.abs(el))

    //somma gradienti
    val mat5 = mat3 + mat4

    val meanGrad = mat5.reduce((a, b) => a + b) / (mat5.size._1 * mat5.size._2)

    println(meanGrad)

    //binarizzazione
    val mat6 = mat5.map(el => if (el > 3 * meanGrad) 1 else 0)

    //media intorno
    val result6 = mat6.conv(filtro5, 0).map(el => if (el >= 4) 1 else 0)
    val result7 = result6.conv(filtro5, 0).map(el => if (el >= 7) 1 else 0)
    val result = result7.conv(filtro5, 0).map(el => if (el >= 7) 255 else 0)

    val out = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)
    pixelIndexes.foreach { case (r, c) => out.setRGB(c, r, result(r, c) * 0x00010101) }

    val pa = file.getParent
    val na = file.getName
    val outFile = pa + "\\out2\\" + na.replaceFirst("\\.", "_" + colore + "_elab_.")
    println("Writing " + outFile)
    ImageIO.write(out, "JPEG", new File(outFile))
  }

  /*
   * returns the cross product of two arrays (as a "sequence of rows")
   * TODO: change impl with flatMap
   */
  def cross[T <: Any](rows: Array[T], columns: Array[T]): Array[(T, T)] = {
    val res = rows.flatMap(elr => columns.map(elc => (elr, elc)))
    res
  }

  //converts an 'int' to an 'rgba pixel'
  def getRGBA(x: Int) = {
    val b = (x) & 0xFF
    val g = (x >> 8) & 0xFF
    val r = (x >> 16) & 0xFF
    val a = (x >> 24) & 0xFF
    (b, g, r, a)
  }
  def setRGBA(x: Tuple4[Int, Int, Int, Int]) = x match {
    case (b, g, r, a) => {
      val b1: Int = (b << 0) | 0x00000000
      val g1: Int = (g << 8) | 0x00000000
      val r1: Int = (r << 16) | 0x00000000
      val a1: Int = (a << 24) | 0x00000000
      (b1 | g1 | r1 | a1)
    }
  }

  def parse(el: Tuple4[Int, Int, Int, Int], component: Int = -1): Int = {
    if (component == -1) {
      (Array[Int](el._1, el._2, el._3).reduce((a, b) => a + b) / 3).toInt
    } else {
      el.productElement(component).asInstanceOf[Int]
    }
  }
}