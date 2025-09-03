// settings.gradle.kts
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral() // Add this if not already present
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "task-management-ktor"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
