package gg.lib.utils

trait ImgUtils {
  /**
   * convert an 'int' to a Tuple4[Int,Int,Int,Int] representing  an 'rgba pixel'
   */
  def getRGBA(x: Int) = {
    val b = (x) & 0xFF
    val g = (x >> 8) & 0xFF
    val r = (x >> 16) & 0xFF
    val a = (x >> 24) & 0xFF
    (b, g, r, a)
  }

  /**
   * convert an 'int' to a Tuple4[Int,Int,Int] representing  an 'rgb pixel'
   */
  def getRGB_A(x: Int) = {
    val b = (x) & 0xFF
    val g = (x >> 8) & 0xFF
    val r = (x >> 16) & 0xFF
    (b, g, r)
  }

  /**
   * convert a Tuple4[Int,Int,Int,Int] representing an 'rgba pixel' to an 'int'
   */
  def int2RGBA(x: Tuple4[Int, Int, Int, Int]) = x match {
    case (b, g, r, a) => {
      val b1: Int = (b << 0) | 0x00000000
      val g1: Int = (g << 8) | 0x00000000
      val r1: Int = (r << 16) | 0x00000000
      val a1: Int = (a << 24) | 0x00000000
      (b1 | g1 | r1 | a1)
    }
  }

  /**
   * convert a Tuple4[Int,Int,Int] representing an 'rgb pixel' to an 'int'
   */
  def int2RGB_A(x: Tuple3[Int, Int, Int]) = x match {
    case (b, g, r) => {
      val b1: Int = (b << 0) | 0x00000000
      val g1: Int = (g << 8) | 0x00000000
      val r1: Int = (r << 16) | 0x00000000
      val a1: Int = (255 << 24) | 0x00000000
      (b1 | g1 | r1 | a1)
    }
  }
  /**
   * convert an 'int' to an 'int' representing a 'grey pixel'
   */
  def int2Grey(x: Int) = {
    x * 0x00010101
  }

  /**
   * define how a pixel should be treated
   */
  def parse(el: Tuple4[Int, Int, Int, Int], component: Int = -1): Int = component match {
    case -1 => {
      (Array[Int](el._1, el._2, el._3).reduce((a, b) => a + b) / 3).toInt
    }
    case index if (index >= 0 && index < 4) => {
      el.productElement(index).asInstanceOf[Int]
    }
    case 4 => {
      math.min(el._3, 255) / 255
    }
    case 5 => {
      (Array[Int](el._1, el._2, el._3).reduce((a, b) => math.max(a, b))).toInt
    }
    case _ => {
      println("ERRORE")
      ???
    }
  }
}