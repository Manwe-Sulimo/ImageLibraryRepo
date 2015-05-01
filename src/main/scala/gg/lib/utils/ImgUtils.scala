package gg.lib.utils

/**
 * Object containing methods to manage (pixel -> whatever) conversions
 *
 * @author Manwe-Sulimo
 *
 */
object ImgUtils {
  /**
   * convert an 'int' to a Tuple4[Int,Int,Int,Int] representing  an 'rgba pixel'
   */
  def RGBA2intTuple(x: Int) = {
    val b = (x) & 0xFF
    val g = (x >> 8) & 0xFF
    val r = (x >> 16) & 0xFF
    val a = (x >> 24) & 0xFF
    (b, g, r, a)
  }

  /**
   * convert a Tuple4[Int,Int,Int,Int] representing an 'rgba pixel' to an 'int'
   */
  def intTuple2RGBA(x: Tuple4[Int, Int, Int, Int]) = x match {
    case (b, g, r, a) => {
      val b1: Int = (b << 0) | 0x00000000
      val g1: Int = (g << 8) | 0x00000000
      val r1: Int = (r << 16) | 0x00000000
      val a1: Int = (a << 24) | 0x00000000
      (b1 | g1 | r1 | a1)
    }
  }

  /**
   * convert an 'int' to an 'int' representing a 'grey pixel'
   */
  def int2Grey(x: Int) = {
    x * 0x00010101
  }
  def double2Grey(x: Double) = {
    x.toInt * 0x00010101
  }

  /**
   * convert an 'int' to an 'int' representing a 'binary pixel'
   */
  def int2binary(x: Int) = {
    (if (x == 0) 0 else 255) * 0x00010101
  }
  def double2binary(x: Double) = {
    (if (x == 0.0) 0 else 255) * 0x00010101
  }

  /**
   * define how a pixel should be treated
   */
  def parse(el: Tuple4[Int, Int, Int, Int], component: Int = -1): Int = component match {
    // mean of rgb components
    case -1 => {
      (Array[Int](el._1, el._2, el._3).reduce((a, b) => a + b) / 3).toInt
    }
    // b|g|r|a component
    case index if (index >= 0 && index < 4) => {
      el.productElement(index).asInstanceOf[Int]
    }
    // max on rgb components
    case 5 => {
      val els = Array[Int](el._1, el._2, el._3)
      els.reduce((a, b) => math.max(a, b)).toInt
    }
    case _ => {
      println("Not yet implemented")
      ???
    }
  }

  def RGBA2DoubleRed(x: Int): Double = {
    val t = RGBA2intTuple(x)
    parse(t, 2).toDouble
  }
  def RGBA2DoubleGreen(x: Int): Double = {
    val t = RGBA2intTuple(x)
    parse(t, 1).toDouble
  }
  def RGBA2DoubleBlue(x: Int): Double = {
    val t = RGBA2intTuple(x)
    parse(t, 1).toDouble
  }
    def RGBA2IntRed(x: Int): Int = {
    val t = RGBA2intTuple(x)
    parse(t, 2)
  }
  def RGBA2IntGreen(x: Int): Int = {
    val t = RGBA2intTuple(x)
    parse(t, 1)
  }
  def RGBA2IntBlue(x: Int): Int = {
    val t = RGBA2intTuple(x)
    parse(t, 1)
  }
}