package bootstrap.liftweb

import net.liftweb._
import util._
import Props._
import Helpers._
import common._
import http._
import sitemap._
import Loc._
import mapper._
import code.model._
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot extends Loggable {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("code")

    db

    siteMap
    
    email

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart = Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd = Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

    // Use jQuery 1.4
    LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQuery14Artifacts
// default resouce bundle
    LiftRules.resourceNames = "i18n/bundle" :: LiftRules.resourceNames 
    
  }

  def db = {
    DefaultConnectionIdentifier.jndiName = "jdbc/JNDI_NAME"

    if (!DB.jndiJdbcConnAvailable_?) {

      val vendor = Props.mode match {
        case RunModes.Test => new StandardDBVendor("org.h2.Driver", "jdbc:h2:mem:lift;DB_CLOSE_DELAY=-1", Empty, Empty)
        case otherwise => new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver", Props.get("db.url") openOr "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE", Props.get("db.user"), Props.get("db.password"))
      }

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    Schemifier.schemify(true, Schemifier.infoF _, User)

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)
  }

  def siteMap = {
    // Build SiteMap
    def sitemap = SiteMap(
      Menu.i("Home") / "index" >> User.AddUserMenusAfter, // the simple way to declare a menu

      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"),
        "Static Content")))

    def sitemapMutators = User.sitemapMutator

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMapFunc(() => sitemapMutators(sitemap))
  }
  
  def email = {
	  
   Mailer.devModeSend.default.set((m: MimeMessage) => logger.info( dump(m.getContent())))
   
   def dump(m: Any) = m match {
      case mm: MimeMultipart => mm.getBodyPart(0).getContent
      case otherwise => otherwise.toString
    }

    //System.setProperty("mail.debug", "true")
    
    import javax.mail.Authenticator
    def optionalAuth: Box[Authenticator] = for { 
           user <- Props.get("mail.user")
           pass <- Props.get("mail.password") 
        } yield new Authenticator {
          override def getPasswordAuthentication = new javax.mail.PasswordAuthentication(user,pass) 
        }
   
    logger.info("Mail authentication with: "+optionalAuth)
    optionalAuth foreach { a => System.setProperty("mail.smtp.auth", "true") }
    Mailer.authenticator = optionalAuth
	  
  }

}
