/*---Plugins---*/
plugins {
    java
    // only essential lombok features like, getter and setter
    id("io.freefair.lombok") version "6.0.0-m2"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

/*---ProjectInfos---*/
group = "de.goldmensch"
version = "3.0-Snapshot"

/*---JavaPluginSettings--*/
java {
    // set sourceCompatibility to 1.8
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

/*---RepositoryRegistration---*/
repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://repository.pretronic.net/repository/pretronic/")
}

/*---DependencyRegistration---*/
dependencies {
    /*--Project--*/
    compileOnly("org.jetbrains:annotations:20.1.0")
    compileOnly("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    implementation("org.bstats", "bstats-bukkit", "2.2.1")

    /*--Test--*/
    testImplementation("junit:junit:4.13")
    testImplementation("net.pretronic.databasequery:pretronicdatabasequery-api:1.1.0.24")
    testImplementation("net.pretronic.databasequery:pretronicdatabasequery-sql:1.1.0.24")
    testImplementation("com.h2database", "h2", "1.4.199")
    testImplementation("org.mariadb.jdbc", "mariadb-java-client", "3.0.0-alpha")

}

/*---SourceSets---*/
sourceSets.main {
    // defines the source set
    java.srcDir("java")

    // defines the resource set
    resources.srcDir("resources")
}

/*---TasksConfiguration---*/
tasks {
    /**
     * Java build settings:
     * - encoding = UTF8
     */
    compileJava {
        options.encoding = "UTF-8"
    }

    /**
     * ShadowJar settings:
     * - relocate bstats to de.goldmensch.bstats
     * - minimize jar
     */
    shadowJar {
        minimize()
        relocate("org.bstats", "de.goldmensch.bstats")
    }

    /*--Tasks--*/
    /**
     * name: buildSpigot
     *
     * Downloads the spigot buildtools and
     * setups a spigot server on for 1.16.5,
     * makes 2 start scripts: windows(batch) and unix(shell);
     * both with a restart loop
     * spigotserverfolder: spigot-server
     */
    register<BuildSpigotTask>("buildSpigot") {
        version = "1.16.5"
    }

    /**
     * name: copyToPlugins
     *
     * copy the built jar to the
     * plugins folder of the spigot server
     */
    register<SmartClansUtils>("copyToPlugins")

    /**
     * name: buildAndCopy
     *
     * builds the project and copy the jar
     * to the plugins folder of the spigot server
     * (executes: buildProject and copyToPlugins)
     */
    register("buildAndCopy") {
        dependsOn("buildProject", "copyToPlugins")
    }

    /**
     * name: test
     *
     * executes the JUnit test with a
     * maximum vm heapSize of 1gb
     */
    test {
        useJUnit()
        maxHeapSize = "1G"
    }

    /**
     * name: buildProject
     *
     * builds the project
     * (executes: shadowJar)
     */
    register("buildProject") {
        dependsOn("clean", "shadowJar")
    }

    /**
     * name: processResource
     *
     * replaced placeholders in plugin.yml
     * placeholders: version, name, api-versionpl
     */
    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("plugin.yml") {
                expand(
                    "version" to project.version,
                    "name" to project.name,
                    "apiVersion" to "1.16"
                )
            }
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }
}



