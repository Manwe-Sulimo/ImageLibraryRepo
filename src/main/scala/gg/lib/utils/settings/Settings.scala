package gg.lib.utils.settings

object Settings {

  private var _isTest: Boolean = false
  def isTest = _isTest
  def isTest_=(value: Boolean) = _isTest = value

  //magic Settings
  val maxThreads = if (sys.runtime.availableProcessors() > 1) sys.runtime.availableProcessors() - 1 else 1
  def rowThresh: Int = if (isTest) 11 else 600
  def colThresh: Int = if (isTest) 12 else 600
  def rowStep: Int = if (isTest) 10 else 400
  def colStep: Int = if (isTest) 15 else 400

  //other magic settings
  val maxLoops = 1000
}