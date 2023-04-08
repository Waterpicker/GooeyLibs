plugins {
    id("gooeylibs.platform")
}

val minecraft = rootProject.property("minecraft")
val fabric = rootProject.property("fabric-api")
version = "$minecraft-$fabric-${rootProject.version}"

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    accessWidenerPath.set(project(":api").file(ACCESS_WIDENER))
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.58.6+1.19.2")
    implementation(project(":api", configuration = "namedElements"))
    "developmentFabric"(project(":api", configuration = "namedElements"))
    bundle(project(":api", configuration = "transformProductionFabric"))
}

tasks {
    processResources {
        inputs.property("version", rootProject.version)

        filesMatching("fabric.mod.json") {
            expand("version" to rootProject.version)
        }
    }
}

publishing {
    repositories {
        maven("https://maven.impactdev.net/repository/development/") {
            name = "ImpactDev-Public"
            credentials {
                username = System.getenv("NEXUS_USER")
                password = System.getenv("NEXUS_PW")
            }
        }
    }

    publications {
        create<MavenPublication>("fabric") {
            from(components["java"])
            groupId = "ca.landonjw.gooeylibs"
            artifactId = "fabric"

            val minecraft = rootProject.property("minecraft")
            val snapshot = rootProject.property("snapshot")?.equals("true") ?: false
            val project = rootProject.property("modVersion")
            version = "$project-$minecraft${if(snapshot) "-SNAPSHOT" else ""}"
        }
    }
}