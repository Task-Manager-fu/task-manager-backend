plugins {
    alias(libs.plugins.kotlin.jvm)
    kotlin("plugin.serialization") version "2.1.10"
}

group = "com.example"
version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val ktor_version = "2.3.3"
val exposed_version = "0.55.0"
val h2_version = "2.1.214"
val logback_version = "1.4.12"


dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-core:${ktor_version}")
    implementation("io.ktor:ktor-server-cio:${ktor_version}")
    implementation("io.ktor:ktor-server-auth:${ktor_version}")
    implementation("io.ktor:ktor-server-auth-jwt:${ktor_version}")
    implementation("io.ktor:ktor-server-content-negotiation:${ktor_version}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktor_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0") // نسخه مناسب
    testImplementation("io.ktor:ktor-server-test-host:${ktor_version}")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.ktor:ktor-server-swagger:${ktor_version}") // نسخه متناسب با Ktorت
    implementation("io.ktor:ktor-server-openapi:${ktor_version}")
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        }
    }


// Database (Exposed + H2 or PostgreSQL)
    implementation("org.jetbrains.exposed:exposed-core:${exposed_version}")
    implementation("org.jetbrains.exposed:exposed-dao:${exposed_version}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${exposed_version}")
    implementation("org.xerial:sqlite-jdbc:3.41.2.1")


    implementation("com.h2database:h2:${h2_version}")

    implementation("org.jetbrains.exposed:exposed-java-time:0.55.0")

// implementation("org.postgresql:postgresql:42.6.0")
// Logger
    implementation("ch.qos.logback:logback-classic:${logback_version}")

    implementation("org.mindrot:jbcrypt:0.4") // Use the latest stable version
    // HikariCP for database connection pooling
    implementation("com.zaxxer:HikariCP:5.1.0")

}
