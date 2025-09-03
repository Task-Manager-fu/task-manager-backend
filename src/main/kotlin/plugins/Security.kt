package com.example.plugins

import com.example.auth.JWTConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.http.*
import io.ktor.server.response.*

fun Application.configureSecurity(jwtConfig: JWTConfig) {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtConfig.realm
            verifier(jwtConfig.verifier)
            validate { cred ->
                val userId = cred.payload.getClaim("userId")?.asInt()
                if (userId != null && userId > 0) JWTPrincipal(cred.payload) else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid or missing token"))
            }
        }
    }
}
