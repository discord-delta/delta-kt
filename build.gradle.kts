import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
}

allprojects {
    group = "to.daedalus"
    version = "1.0.0"

    repositories {
        mavenCentral()

        maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
    }

    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        testImplementation(kotlin("test-junit"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    }

    tasks.test {
        useJUnit()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}