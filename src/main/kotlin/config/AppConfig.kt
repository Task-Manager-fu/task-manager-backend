package com.example.config
import io.ktor.server.config.*

data class AppConfig(
    val jwt: Jwt,
    val database: Db
) {
    data class Jwt(
        val secret: String,
        val issuer: String,
        val audience: String,
        val realm: String,
        val expiresInSec: Long
    )
    data class Db(
        val url: String,
        val driver: String,
        val user: String,
        val password: String
    )

    companion object {
        fun from(conf: ApplicationConfig): AppConfig {
            val jwt = Jwt(
                secret = conf.propertyOrNull("ktor.security.jwt.secret")?.getString() ?: "mySuperSecretKey",
                issuer = conf.property("ktor.security.jwt.issuer").getString(),
                audience = conf.property("ktor.security.jwt.audience").getString(),
                realm = conf.property("ktor.security.jwt.realm").getString(),
                expiresInSec = conf.property("ktor.security.jwt.expiresInSec").getString().toLong()
            )
            val db = Db(
                url = conf.property("ktor.database.url").getString(),
                driver = conf.property("ktor.database.driver").getString(),
                user = conf.property("ktor.database.user").getString(),
                password = conf.property("ktor.database.password").getString()
            )
            return AppConfig(jwt, db)
        }
    }
}
