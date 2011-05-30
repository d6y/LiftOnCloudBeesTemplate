import sbt._

 class MySbtProjectPlugins(info: ProjectInfo) extends PluginDefinition(info) {
    
	
	// repositories
	lazy val sonatypeRepo = "sonatype.repo" at "https://oss.sonatype.org/content/groups/public"
	lazy val scctRepo = "scct-repo" at "http://mtkopone.github.com/scct/maven-repo/"	
	
	lazy val eclipse = "de.element34" % "sbt-eclipsify" % "0.7.0"
    lazy val scctPlugin = "reaktor" % "sbt-scct-for-2.8" % "0.1-SNAPSHOT"
    lazy val cloudbees = "eu.getintheloop" % "sbt-cloudbees-plugin" % "0.2.7"
    
 }