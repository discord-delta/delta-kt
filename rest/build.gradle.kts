plugins {
    kotlin("plugin.serialization") version "1.4.20"
}

dependencies {
    api(project(":common"))
    api(project(":request"))
    api(project(":entity"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    implementation("io.ktor:ktor:1.4.3")
    implementation("io.ktor:ktor-client-cio:1.4.3")
}