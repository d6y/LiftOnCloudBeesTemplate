package code.snippet

import java.util.Calendar

import scala.xml._

import net.liftweb.util.Helpers._

class DateTime {

  def render  = Text(Calendar.getInstance.get(Calendar.YEAR).toString)
  
}
	