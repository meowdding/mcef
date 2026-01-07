plugins {
    id("maven-publish")
    id("idea")
    id("java")
    `jcef-loom`
    `versioned-catalogues`
}

tasks.register("cloneJcef", Exec::class.java) {
    commandLine("git", "submodule", "update", "--init", "--recursive", "common/java-cef")
}

sourceSets {
    val jcef = create("jcef") {
        java {
            srcDir(rootProject.files("java-cef/java"))
            exclude { it.relativePath.contains("test") }
        }
    }

    main {
        compileClasspath += jcef.output
        runtimeClasspath += jcef.output
    }
}
tasks.processResources {
    dependsOn(tasks.named("processJcefResources"))
}

tasks.jar {
    from(sourceSets.named("jcef").map { it.output.classesDirs })
    from(sourceSets.named("jcef").map { it.output.resourcesDir })
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

idea {
    module {
        excludeDirs.addAll(files("java-cef/java/tests"))
        inheritOutputDirs = true
    }
}

dependencies {
    minecraft(versionedCatalog["minecraft"])
    mappings(loom.layered {
        officialMojangMappings()
        parchment(variantOf(versionedCatalog["parchment"]) {
            artifactType("zip")
        })
    })

    modImplementation(libs.fabric.loader)
    modImplementation(versionedCatalog["fabric.api"])
}