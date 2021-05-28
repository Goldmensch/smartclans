import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

open class BuildSpigotTask : DefaultTask() {

    @Input
    lateinit var version: String

    @TaskAction
    fun build() {
        if (!buildToolsDir.exists() || !buildToolsFile.exists()) {
            buildToolsDir.mkdirs()
            buildToolsDir.resolve(buildToolsFile).writeBytes(buildToolsUrl.readBytes())
        }

        cmd(
            "java",
            "-jar",
            buildToolsFile.name,
            "--rev",
            version,
            directory = buildToolsDir,
            printToStdout = true
        )

        val javaCmd = "java -Xmx1G -Xms1G -jar spigot-$version.jar -nogui"

        println("build start scripts")
        File(buildToolsDir.path + File.separator + "start.bat").writeText(
            ":loop \n" +
                    javaCmd + "\n" +
                    "goto loop"
        )

        File(buildToolsDir.path + File.separator + "start.sh").writeText(
            "while true\n" +
                    "do\n" +
                    javaCmd + "\n" +
                    "done"
        )

        println("cleanup")
        buildToolsDir.listFiles().filter { file ->
            file.isDirectory || (file.name.equals("BuildTools.jar")
                    || file.name.equals("BuildTools.log.txt"))
        }.forEach { file -> file.deleteRecursively() }
    }
}
