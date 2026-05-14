import net.minecrell.pluginyml.paper.PaperPluginDescription
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ofPattern

plugins {
    id("java")
    alias(libs.plugins.catalog.updater)
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.plugin.yml.paper)
}

group = "com.originsrebirth"
version = "1.0-" + LocalDate.now().format(ofPattern("yyyyMMdd"))

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    // To pull OriginsEssentials from local Maven
//    exclusiveContent {
//        filter {
//            includeModule("com.originsrebirth", "originsessentials")
//        }
//        forRepository {
//            mavenLocal()
//        }
//    }
    // Add more repositories as needed for your dependencies below here
    // Ensure you use /gradle/libs.versions.toml
}

dependencies {
    paperweight.paperDevBundle(libs.versions.minecraft.get() + ".build.+")

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    
    // Add your dependencies below here
    // Ensure you use /gradle/libs.versions.toml
}

// --- No need to touch below this line! Unless uncommenting the DependencyLoader, Load or adding serverDependencies ---

val targetJavaVersion = libs.versions.jdk.get().toInt()
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
}

tasks {
    runServer {
        minecraftVersion(libs.versions.minecraft.get())
        systemProperty("net.kyori.adventure.text.warnWhenLegacyFormattingDetected", false)
    }
    build {
        finalizedBy(runServer)
	}
    compileJava {
        dependsOn(versionCatalogUpdate)
        options.encoding = Charsets.UTF_8.name()
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
            options.release.set(targetJavaVersion)
        }
    }
    javadoc {
        val options = options as StandardJavadocDocletOptions
        options.addMultilineStringsOption("tag").setValue(listOf("OriginalPackageName", "OriginalProjectName", "ClassFileCreated", "author"))
        options.addBooleanOption("author", true)
        options.encoding = Charsets.UTF_8.name()
    }
}

paper {
    name = project.name
    version = project.version.toString()
    main = "com.originsrebirth.spigot." + project.name.lowercase().replace("origins", "") + "." + project.name.lowercase().replace("origins", "").capitalize()
    apiVersion = libs.versions.minecraft.get()
    //loader = "com.originsrebirth.spigot." + project.name.lowercase().replace("origins", "") + ".util.DependencyLoader"
    description = ""
    authors = listOf("FlynCodes")
    //load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    serverDependencies {
//        register("ExamplePlugin") {
//            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
//            required = false
//        }
    }
}

tasks.withType(Jar::class) {
    manifest {
        attributes["Built-For"] = "Origins Rebirth"
        attributes["url"] = "https://originsrebirth.com/"
        attributes["Built-By"] = "FlynCodes <admin[at]flyn.codes> at Origins Rebirth"
        attributes["license"] = "Proprietary, not for distribution. Do not share without permission."
    }
}
