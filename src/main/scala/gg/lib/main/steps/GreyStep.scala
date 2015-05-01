package gg.lib.main.steps

import java.awt.image.BufferedImage
import java.io.File
import java.io.FileFilter
import java.util.logging.Logger

import gg.lib.img.Images.readImage
import gg.lib.img.Images.writeImage
import gg.lib.linalg.general2.MatrixDense
import gg.lib.utils.ImgUtils

trait GreyStep extends Step[Int] {
  private val log: Logger = Logger.getGlobal()
  private val fileFilter = new FileFilter() {
    def accept(file: File): Boolean = {

      if (file.isDirectory()) {
        false
      } else {
        val name = file.getName()
        name.contains(".jpeg") || name.contains(".png")
      }
    }
  }

  override def run(inDir: String, outDir: String, byte2entry: Int => Int): Boolean = {
    val iD = new File(inDir)
    val oD = new File(outDir)
    if (!iD.isDirectory() || !oD.isDirectory()) {
      log.warning(inDir + " or " + outDir + " is not a directory: how exciting! I will exit ...")
      false
    } else {
      val fileList = iD.listFiles(fileFilter)
      fileList.foreach(file => {
        val startingMatrix = read(file.getAbsolutePath(), byte2entry)
        val result = compute(startingMatrix)
        write(result, oD.getAbsolutePath() + "\\" + file.getName().replaceFirst("\\.jpeg", ".png"))
      })

      true
    }
  }

  // read the image at the given path into a matrix
  override def read(inPath: String, byte2entry: Int => Int): MatrixDense[Int] = {
    readImage(inPath, byte2entry)
  }

  // do some stuff on the given matrix and return the result
  override def compute(matrix: MatrixDense[Int]): MatrixDense[Int]

  // write the given matrix to an image at the specified path
  // always write as binary png image
  override def write(matrix: MatrixDense[Int], outPath: String): Unit = {
    val f: Int => Int = x => if(x==0) 0 else x*0x00010101
    writeImage(matrix, outPath, "png", BufferedImage.TYPE_BYTE_GRAY, f)
  }
}