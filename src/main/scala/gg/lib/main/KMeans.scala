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
import gg.lib.linalg.DSDouble3
import scala.util.Random
import gg.lib.utils.SetUtils
import java.io.PrintWriter

object KMeans extends ImgUtils with SetUtils {
  implicit val trasfDSDouble3: ((Double, Double, Double)) => (DSDouble3) = (x) => DSDouble3(x)

  implicit val ai2ad: Array[(Int, Int, Int)] => Array[(Double, Double, Double)] = li => li.map(x => (x._1.toDouble, x._2.toDouble, x._3.toDouble))

  implicit val fi2fd: (((Int, Int, Int)) => (Int)) => (((Double, Double, Double)) => (Int)) = f => {
    x => f(x._1.toInt, x._2.toInt, x._3.toInt)
  }

  private val log: Logger = Logger.getGlobal()

  type MC = scala.collection.mutable.Map[Int, DSDouble3]
  type CC = scala.collection.mutable.Map[Int, List[DSDouble3]]

  def execute(inDir: String, outDir: String, inNumCenters: Int, inNumIters: Int, inEps: Double) = {

    // read image files
    val dir = new File(inDir)
    val files = dir.listFiles().filter(el => el.getName().contains("png")).map(el => el.getPath)

    // load images, parse data, save results
    files.foreach(filePath => {

      val fileName = new File(filePath).getName()
      val outFile = outDir + fileName.split('.')(0) + ".png"

      log.log(Level.INFO, "Starting with " + fileName)

      val (img, pixelIndexes) = read(filePath)
      val width = img.getWidth
      val height = img.getHeight

      val matrix = new DMatrix[(Double, Double, Double)](height, width, pixelIndexes.map { case (row, col) => getRGB_A(img.getRGB(col, row)) })

      val result = compute(matrix, inNumCenters, inNumIters, inEps)
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
  def compute(matrix: DMatrix[(Double, Double, Double)], numCenters: Int, numIters: Int, eps: Double): DMatrix[(Double, Double, Double)] = {
    log.log(Level.INFO, "Starting compute")

    val m = matrix.size._1
    val n = matrix.size._2

    log.log(Level.INFO, "Initializing startingEls")
    val startingEls = matrix.collect.map(el => (DSDouble3(el), -1, 0.0))
    val (finalElements, finalCenters) = update(startingEls, randomCenters(matrix, numCenters, m, n), numIters, eps, true)
    //TODO write results and errors

    //return final matrix
    val resultingValues = finalElements.map(el => finalCenters.get(el._2).get.get)
    new DMatrix(m, n, resultingValues)
  }

  /**
   * write
   */
  def write(result: DMatrix[(Double, Double, Double)], pixelIndexes: Array[(Int, Int)], outPath: String) = {
    //    val pw: PrintWriter = new PrintWriter(outPath.replace("png", "log"))
    //    pw.write(result.toString)
    //    pw.close
    Images.writeImage(result, pixelIndexes, outPath, "png", BufferedImage.TYPE_INT_RGB, int2RGB_A)
  }

  /**
   * compute aux methods
   */
  def randomCenters(matrix: DMatrix[(Double, Double, Double)], numCenters: Int, width: Int, height: Int): MC = {
    val rand = new Random
    def r(x: Int) = math.abs(rand.nextInt(x))
    def randomEntry = matrix(r(width), r(height))
    var result = scala.collection.mutable.Map[Int, DSDouble3]()
    List.range(0, numCenters).foreach(i => result.+=((i, randomEntry)))
    result
  }

  //start update function
  def update(elements: Array[(DSDouble3, Int, Double)], centers: MC, numIters: Int, eps: Double, toContinue: Boolean): (Array[(DSDouble3, Int, Double)], MC) = {

    log.log(Level.INFO, "Starting update. Remaining iterations: " + numIters)
    if (!toContinue || numIters == 0) {
      log.log(Level.INFO, "Ended update with toContinue = " + toContinue + " and numIters = " + numIters)
      log.log(Level.INFO, centers.toString)
      (elements, centers)
    } else {
      val ccMap: CC = scala.collection.mutable.Map[Int, List[DSDouble3]]()
      0 until centers.size foreach (i => ccMap += ((i, Nil)))

      val newElements = elements.map(updateEntry(_, centers, ccMap))
      val newCenters = nextCenters(centers, ccMap)
      val continue = tolerance(centers, newCenters, eps)

      update(newElements, newCenters, numIters - 1, eps, continue)
    }
  }
  //end update function

  def updateEntry(entry: (DSDouble3, Int, Double), oldCenters: MC, ccMap: CC): (DSDouble3, Int, Double) = {
    val e1 = entry._1
    val (index, distance) = oldCenters map { case (key, value) => (key, dist(e1, value)) } reduce ((a, b) => if (a._2 < b._2) a else b)

    val tmp = ccMap.get(index)
    val oldMapValue = tmp.get
    ccMap update (index, e1 :: oldMapValue)

    (e1, index, distance)
  }

  def nextCenters(oldCenters: MC, ccMap: CC): MC = {
    //centerUpdateFunction
    def cuf: (DSDouble3, List[DSDouble3]) => DSDouble3 = (el, list) => {
      val grad = (el + (list reduce ((a, b) => a + b) dotDiv (list.length)).neg)
      el + ((grad dotDiv grad.norm) * DSDouble3((0.1, 0.1, 0.1)))
    }
    ccMap.map(el => el match {
      case (key1, Nil) => (key1, oldCenters.get(key1).get)
      case (key2, list) => (key2, cuf(oldCenters.get(key2).get, list))
    })
  }

  def tolerance(oldCenters: MC, updatedCenters: MC, eps: Double): Boolean = {
    log.log(Level.INFO, "Starting tolerance")
    def get(map: MC, index: Int) = { if (map.get(index) == None) DSDouble3((0, 0, 0)) else map.get(index).get }
    val imp = oldCenters.keys.map(i => dist(get(oldCenters, i), get(updatedCenters, i))).reduce((a, b) => a + b) / (oldCenters.size)
    if (imp > eps) true else false
  }
}