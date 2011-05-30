import sbt._
import de.element34.sbteclipsify._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) with Eclipsify  with bees.RunCloudPlugin {
  val liftVersion = "2.3"

  override def beesUsername = Some("username")
  override def beesApplicationId = Some("username/appname")

	  
  // uncomment the following if you want to use the snapshot repo
  //  val scalatoolsSnapshot = ScalaToolsSnapshots
  lazy val liftModulesRelease = "liftmodules repository" at " http://repository-liftmodules.forge.cloudbees.com/release/"

  override def jettyWebappPath = webappPath
  override def scanDirectories = Nil

  
  
  override def libraryDependencies = Set(
    
	//lift	  
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default" withSources(),
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default" withSources(),
    "ch.qos.logback" % "logback-classic" % "0.9.26",

    //optional modules
  	"net.liftmodules" %% "google-analytics" % (liftVersion+"-0.9"),
    
    // database 
    "mysql" % "mysql-connector-java" % "5.1.14",    
    "com.h2database" % "h2" % "1.2.138" % "test",
    //testing
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test",
    "junit" % "junit" % "4.5" % "test",
    "org.scala-tools.testing" %% "specs" % "1.6.6" % "test",
    
    //Provided
    "javax.servlet" % "servlet-api" % "2.5" % "provided->default"
  ) ++ super.libraryDependencies
}