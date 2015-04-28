//package gg.lib.main
//
//import gg.lib.linalg.DMatrix
//import java.awt.image.BufferedImage
//import gg.lib.linalg.Intero
//import gg.lib.img.Images
//import java.util.logging.Logger
//import java.io.File
//import java.util.logging.Level
//import gg.lib.utils.ImgUtils
//import gg.lib.linalg.Decimale
//import gg.lib.utils.LargeConvolution
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//object SecondStep extends ImgUtils {
//  implicit val trasfInt: (Double) => (Decimale) = x => Decimale(x)
//  implicit val fi2fd: ((Int) => (Int)) => ((Double) => (Int)) = {
//    f => x: Double => f(x.toInt)
//  }
//
//  private val log: Logger = Logger.getGlobal()
//
//  def execute(inDir: String, outDir: String) = {
//    val pool: ExecutorService = Executors.newFixedThreadPool(7)
//    // read image files
//    val dir = new File(inDir)
//    val files = dir.listFiles().filter(el => el.getName().contains("jpeg")).map(el => el.getPath)
//    try {
//      // load images, parse data, save results
//      files.foreach(filePath => {
//
//        val fileName = new File(filePath).getName()
//
//        val (img, pixelIndexes) = read(filePath)
//        val width = img.getWidth
//        val height = img.getHeight
//
//        log.log(Level.INFO, "Starting white")
//
//        val elementsWhite = pixelIndexes.map { case (row, col) => parse(getRGBA(img.getRGB(col, row)), 4).toDouble }
//        val w = new DMatrix[Double](height, width, elementsWhite)
//        log.log(Level.INFO, "Starting computation")
//        val wc = compute(w, height, width, pool)
//        log.log(Level.INFO, "Ended computation")
//        write(wc, pixelIndexes, outDir + fileName.replaceFirst("jpeg", "png"))
//
//        log.log(Level.INFO, "Ended  white")
//      })
//    } finally {
//      pool.shutdown()
//    }
//  }
//
//  /**
//   * read
//   */
//  def read(filePath: String): (BufferedImage, Array[(Int, Int)]) = {
//    Images.readImage(filePath)
//  }
//
//  /**
//   * compute
//   */
//  def compute(matrix: DMatrix[Double], height: Int, width: Int, pool: ExecutorService) = {
//
//    // filtro 0
//    val filtro0 = new DMatrix[Double](3, 3, Array(1, 1, 1,
//      1, 0, 1,
//      1, 1, 1))
//
//    // filtro 1
//    val filtro1 = new DMatrix[Double](3, 3, Array(1, 0, 0,
//      0, 1, 0,
//      0, 0, 1))
//
//    // filtro 2
//    val filtro2 = new DMatrix[Double](3, 3, Array(0, 1, 0,
//      0, 0, 0,
//      0, 1, 0))
//
//    // filtro 3
//    val filtro3 = new DMatrix[Double](3, 3, Array(0, 0, 1,
//      0, 0, 0,
//      1, 0, 0))
//
//    // filtro 4
//    val filtro4 = new DMatrix[Double](3, 3, Array(0, 0, 0,
//      1, 0, 1,
//      0, 0, 0))
//    // soglia intorno  
//    println("FIRST CONV")
//    val mat0 = new LargeConvolution(matrix, filtro0, 0, pool).call.map(el => if (el >= 1) 1.0 else 0.0)
//    System.gc()
//    println("SECOND CONV")
//    // applicazione filtro
//    val mat1 = new LargeConvolution(mat0, filtro1, 0, pool).call.map(el => if (el >= 1) 1.0 else 0.0)
//    System.gc()
//    println("THIRD CONV")
//    System.gc()
//    // applicazione filtro
//    val mat2 = new LargeConvolution(mat0, filtro2, 0, pool).call.map(el => if (el >= 1) 1.0 else 0.0)
//    println("FOURTH CONV")
//    System.gc()
//    // applicazione filtro
//    val mat3 = new LargeConvolution(mat0, filtro3, 0, pool).call.map(el => if (el >= 1) 1.0 else 0.0)
//    println("FIFTH CONV")
//    System.gc()
//    // applicazione filtro
//    val mat4 = new LargeConvolution(mat0, filtro4, 0, pool).call.map(el => if (el >= 1) 1.0 else 0.0)
//
//    println("SUM RESULTS")
//    System.gc()
//    // somma risultati
//    val mat = (mat1 + mat2 + mat3 + mat4).map(el => if (el > 0) 1.0 else 0.0)
//
//    //    // applicazione filtro
//    //    println("SEVENTH CONV")
//    //    System.gc()
//    //    val mat5 = new LargeConvolution(mat, filtro1, 0, pool).call.map(el => if (el >= 2) 1.0 else 0.0)
//    //    // applicazione filtro
//    //    println("EIGTH CONV")
//    //    System.gc()
//    //    val mat6 = new LargeConvolution(mat, filtro2, 0, pool).call.map(el => if (el >= 2) 1.0 else 0.0)
//    //    println("NINTH CONV")
//    //    System.gc()
//    //    // applicazione filtro
//    //    val mat7 = new LargeConvolution(mat, filtro3, 0, pool).call.map(el => if (el >= 2) 1.0 else 0.0)
//    //    println("TENTH CONV")
//    //    System.gc()
//    //    // applicazione filtro
//    //    val mat8 = new LargeConvolution(mat, filtro4, 0, pool).call.map(el => if (el >= 2) 1.0 else 0.0)
//
//    val result = mat.map(el => if (el > 0) 255.0 else 0.0)
//    result
//
//  }
//
//  /**
//   * write
//   */
//  def write(matrix: DMatrix[Double], pixelIndexes: Array[(Int, Int)], outPath: String) = {
//    Images.writeImage(matrix, pixelIndexes, outPath, "png", BufferedImage.TYPE_BYTE_GRAY, int2Grey)
//  }
//
//}