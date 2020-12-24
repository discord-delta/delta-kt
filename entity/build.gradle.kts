plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.20"
}

dependencies {
    api(project(":common"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
}
