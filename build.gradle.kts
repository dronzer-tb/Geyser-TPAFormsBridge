plugins {
    java
    id("com.gradleup.shadow") version "8.3.5"
}

group = "org.tpaforms"
version = "1.0.0"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.essentialsx.net/releases/")
    maven("https://repo.opencollab.dev/main/")
    maven("https://jitpack.io")
}

dependencies {
    // Paper/Spigot API
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    // EssentialsX API
    compileOnly("net.essentialsx:EssentialsX:2.20.1")

    // Floodgate API (only for Bedrock player detection)
    compileOnly("org.geysermc.floodgate:api:2.2.3-SNAPSHOT")

    // Gson for JSON serialization
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.shadowJar {
    archiveClassifier.set("")
    archiveFileName.set("TPAFormsBridge.jar")
    relocate("com.google.gson", "org.tpaforms.libs.gson")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
