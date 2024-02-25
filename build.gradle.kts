plugins {
    id("fabric-loom") version "1.5-SNAPSHOT" // Fabric Loom
    id("io.github.p03w.machete") version "1.1.4" // Build jar compression
    id("me.modmuss50.mod-publish-plugin") version "0.4.5" // Mod publishing

    `maven-publish` // Maven publishing
    java
}
version = "${property("minecraft_version")}-${property("mod_version")}"
group = property("maven_group").toString()

base {
    archivesName = "${property("archives_base_name")}-Fabric"
}

repositories {
    maven("https://maven.parchmentmc.org") // Parchment mappings
    maven("https://dvs1.progwml6.com/files/maven/") // JEI
    maven("https://maven.tterrag.com") // The One Probe
    maven("https://maven.theillusivec4.top") // Curios
    maven("https://modmaven.dev") // JEI
    maven("https://maven.izzel.io/releases") // Modern UI
    maven("https://maven.terraformersmc.com") // EMI, Trinkets
    maven("https://maven.shedaniel.me") // REI
    maven("https://maven.shurik.me/releases") // simple-chunk-manager
    maven("https://cursemaven.com") { // GregTechCEu
        content { includeGroup("curse.maven") }
    }
    maven("https://mvn.devos.one/snapshots") // Porting Lib
    maven("https://jitpack.io") { // Mixin Extras (Porting Lib)
        content { excludeGroup("io.github.fabricators_of_create.Porting-Lib") }
    }
    maven("https://maven.ladysnake.org/releases") // Cardinal Components (Trinkets)
    maven ("https://maven.jamieswhiteshirt.com/libs-release") {
        content { includeGroup("com.jamieswhiteshirt") }
    }
    maven("https://maven.architectury.dev") { // Architectury
        content { includeGroup("dev.architectury") }
    }
//    maven { url 'https://maven.firstdarkdev.xyz/snapshots' } // LDLib (GregTechCEu)
//    maven { // KubeJS and Rhino (LDLib)
//        url 'https://maven.saps.dev/releases'
//        content { includeGroup "dev.latvian.mods" }
//    }
}



dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")

    mappings(loom.layered {
        officialMojangMappings()

        val parchmentVersion = property("parchment_version").toString()
        if (parchmentVersion.contains(":"))
            // Use exact version
            parchment("org.parchmentmc.data:parchment-${parchmentVersion}@zip")
        else
            // Use minecraft version + given date
            parchment("org.parchmentmc.data:parchment-${property("minecraft_version")}:${parchmentVersion}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    // Fabric API
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

//    modCompileOnly("curse.maven:gregtechceu-modern-890405:4676506") // GregTechCEu 1.20.1-1.0.10-forge

    // EMI
    modCompileOnly("dev.emi:emi-fabric:${property("emi_version")}:api") { isTransitive = false }
    // JEI
    modCompileOnly("mezz.jei:jei-${property("minecraft_version")}-fabric:${property("jei_version")}") { isTransitive = false }
    // REI
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:${property("rei_version")}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-fabric:${property("rei_version")}")
    modCompileOnly("dev.architectury:architectury-fabric:${property("architectury_version")}") // For REI
    modApi("me.shedaniel.cloth:basic-math:+") // For REI

    modLocalRuntime("me.shedaniel:RoughlyEnoughItems-fabric:${property("rei_version")}")
    modLocalRuntime("dev.architectury:architectury-fabric:${property("architectury_version")}") // For REI


    modImplementation("icyllis.modernui:ModernUI-Core:${property("modernui_core_version")}") {
        exclude("org.apache.logging.log4j", "log4j-core")
        exclude("org.apache.logging.log4j", "log4j-api")
        exclude("com.google.code.findbugs", "jsr305")
        exclude("org.jetbrains", "annotations")
        exclude("com.ibm.icu", "icu4j")
        exclude("it.unimi.dsi", "fastutil")
    }
    modImplementation("icyllis.modernui:ModernUI-Markdown:${property("modernui_core_version")}") {
        exclude("org.apache.logging.log4j", "log4j-core")
        exclude("org.apache.logging.log4j", "log4j-api")
        exclude("com.google.code.findbugs", "jsr305")
        exclude("org.jetbrains", "annotations")
        exclude("com.ibm.icu", "icu4j")
        exclude("it.unimi.dsi", "fastutil")
    }
    modImplementation("icyllis.modernui:ModernUI-Fabric:${property("minecraft_version")}-${property("modernui_fabric_version")}")

    modImplementation(include("teamreborn:energy:${property("tr_energy_version")}") {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    })

    modImplementation(include("me.shurik:simple-chunk-manager:0.2.7")) {}

    modImplementation(include("io.github.fabricators_of_create.Porting-Lib:base:${property("porting_lib_version")}")) {}
    modImplementation(include("io.github.fabricators_of_create.Porting-Lib:items:${property("porting_lib_version")}")) {}
    modImplementation(include("io.github.fabricators_of_create.Porting-Lib:blocks:${property("porting_lib_version")}")) {}
    modImplementation(include("io.github.fabricators_of_create.Porting-Lib:config:${property("porting_lib_version")}")) {}

//    modImplementation "curse.maven:gregtechceu-modern-890405:${gtceu_file_id}"

    modImplementation("dev.emi:trinkets:${property("trinkets_version")}")

    // for Trinkets
    modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${property("cca_version")}")
    modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${property("cca_version")}")

    // for Trinkets
    modRuntimeOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${property("cca_version")}")
    modRuntimeOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${property("cca_version")}")

    // Jade
    modImplementation("curse.maven:jade-324717:${property("jade_file_id")}")

    // for GregTechCEu
    // modCompileOnly("com.lowdragmc.ldlib:ldlib-common-1.20.1:${property("ldlib_version")}")
    // modCompileOnly("com.lowdragmc.ldlib:ldlib-fabric-1.20.1:${property("ldlib_version")}")

    // I got tiered of creating a new network every time. ¯\_(ツ)_/¯
    modImplementation("com.ptsmods:devlogin:3.4.1")

    implementation("com.google.code.findbugs:jsr305:3.0.2")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("loader_version", project.properties["loader_version"])
        inputs.property("fabric_version", project.properties["fabric_version"])
        inputs.property("java_version", java.targetCompatibility.majorVersion)
        //    filteringCharset "UTF-8"

    filesMatching("fabric.mod.json") {
        expand("version" to  project.version,
                "loader_version" to project.properties["loader_version"],
                "fabric_version" to project.properties["fabric_version"],
                "java_version" to java.targetCompatibility.majorVersion)
    }
}

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        withSourcesJar()
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName.get()}"}
        }
    }

    publishMods {
        file = remapJar.get().archiveFile
        changelog = providers.environmentVariable("CHANGELOG").getOrElse("No changelog provided")
        type = STABLE
        displayName = "Flux Networks ${property("minecraft_version")} ${property("mod_version")} [Fabric]"
        modLoaders.add("fabric")
        dryRun = providers.environmentVariable("CI").getOrNull() == null

        curseforge {
            accessToken = providers.environmentVariable("CURSEFORGE_API_KEY")
            projectId = "962362"
            minecraftVersions.add(property("minecraft_version").toString())
            requires("fabric-api")
            optional("modern-ui")
            optional("jade")
            optional("trinkets")
            optional("jei")
            optional("roughly-enough-items")
        }
        modrinth {
            accessToken = providers.environmentVariable("MODRINTH_TOKEN")
            projectId = "d1ItuIJe"
            minecraftVersions.add(property("minecraft_version").toString())
            requires("fabric-api")
            optional("modern-ui")
            optional("jade")
            optional("trinkets")
            optional("jei")
            optional("rei")
        }

        // Fix machete compression
        getTasksByName("publishModrinth", true).forEach {
            it.dependsOn("optimizeOutputsOfRemapJar")
        }
        getTasksByName("publishCurseforge", true).forEach {
            it.dependsOn("optimizeOutputsOfRemapJar")
        }
    }
}