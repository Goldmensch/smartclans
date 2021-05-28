import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class SmartClansUtils : DefaultTask() {
    @TaskAction
    fun copy() {
        val buildPath = "build" + File.separator + "libs" + File.separator + "smartclans-" + project.version + "-all.jar"
        if (!File(buildPath).exists()) {
            println("build plugin first")
            return
        }
        val des = File(buildToolsDir.path + File.separator + "plugins" + File.separator + "smartclans.jar")
        des.mkdirs()
        File(buildPath).copyTo(des, true)
    }

}