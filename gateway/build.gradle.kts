plugins {
    kotlin("plugin.serialization") version "1.4.20"
}

dependencies {
    implementation(project(":entity"))
    implementation(project(":request"))

    implementation("io.ktor:ktor:1.4.3")
    implementation("io.ktor:ktor-client-cio:1.4.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
}