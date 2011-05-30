package code.snippet

import scala.xml._

object Html5Shim {
	
	def render = Unparsed("""<!--[if lt IE 9]><script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->""")

}

