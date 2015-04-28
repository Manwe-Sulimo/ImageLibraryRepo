package gg.lib.main.steps

import gg.lib.linalg.general2.MatrixDense

/**
 * Trait definition of a Step
 *
 * @author Manwe-Sulimo
 *
 */
trait Step[T] {
  def run(inDir: String, outDir: String, byte2entry: Int => T): Boolean
  def read(inPath: String, byte2entry: Int => T): MatrixDense[T]
  def compute(matrix: MatrixDense[T]): MatrixDense[T]
  def write(matrix: MatrixDense[T], outPath: String): Unit
}