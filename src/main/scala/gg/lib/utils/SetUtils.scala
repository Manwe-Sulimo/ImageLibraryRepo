package gg.lib.utils

/**
 * Misc
 *
 * @author Manwe-Sulimo
 *
 */
object SetUtils {

  // returns the cross product of two arrays (as a "sequence of rows")
  def cross[T <: Any](rows: Array[T], columns: Array[T]): Array[(T, T)] = {
    val res = rows.flatMap(elr => columns.map(elc => (elr, elc)))
    res
  }

  // returns the cross product of two arrays (as a "sequence of rows")
  def cross[T <: Any](rows: List[T], columns: List[T]): List[(T, T)] = {
    val res = rows.flatMap(elr => columns.map(elc => (elr, elc)))
    res
  }

}