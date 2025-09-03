package com.example.plugins

import com.example.auth.AuthService
import com.example.auth.authRoutes
import com.example.exceptions.configureExceptionHandling
import com.example.user.User
import com.example.user.UserService
import com.example.user.userRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting(authService: AuthService, userService: UserService) {

    routing {
        get("/") {
            call.respondText("Welcome 🚀")
            call.configureExceptionHandling {
                // کد اصلی route
                val value = listOf<String>().first() // مثال خطا
                call.respondText("Value: $value")
            }
        }

        authRoutes(authService)
        userRoutes(userService)
    }
}
