package com.example.auth

import com.example.user.UserService
import com.example.utils.HashUtil
import com.example.exceptions.BadRequestException
import com.example.exceptions.configureExceptionHandling
import com.example.models.LoginRequest
import com.example.models.RegisterRequest
import com.example.models.TokenResponse
import com.example.models.UserResponse
import com.example.utils.respondSuccessMessage
import com.google.gson.Gson
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.util.UUID

inline fun <reified T> ApplicationCall.receiveFromJson(text: String): T {
    return Gson().fromJson(text, T::class.java)
}
fun Route.authRoutes(authService: AuthService , passwordResetService: PasswordResetService) {
    route("/auth") {
        post("/register") {
            call.configureExceptionHandling {
                val request = call.receive<RegisterRequest>()
                val result = authService.register(request)
                call.respond(HttpStatusCode.OK, result)
            }
        }
        post("/login") {
            call.configureExceptionHandling {
               val body= call.receiveFromJson<LoginRequest>(
                    call.receiveText()
                )
                val token = authService.login(body)
                call.respond(token)
            }

        }

        post("/request-reset-password") {
            call.configureExceptionHandling {
                val request = call.receive<RequestResetPasswordDTO>()
                passwordResetService.requestReset(request.email)
                call.respondSuccessMessage(HttpStatusCode.OK, "If this email exists, a reset link has been sent")
            }
        }

        post("/reset-password") {
            call.configureExceptionHandling {
                val request = call.receive<ResetPasswordDTO>()
                passwordResetService.resetPassword(request.token, request.newPassword)
                call.respondSuccessMessage(HttpStatusCode.OK, "Password successfully reset")
            }
        }


    }
}

