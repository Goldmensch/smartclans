import java.io.File
import java.net.URL

val buildToolsDir = File("spigot-server")
val buildToolsFile = File("BuildTools.jar")
val buildToolsUrl =
    URL("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar")
