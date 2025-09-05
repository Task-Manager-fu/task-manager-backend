package com.example.user
import com.example.exceptions.AppException
import com.example.exceptions.UnauthorizedException
import com.example.exceptions.configureExceptionHandling
import io.ktor.client.request.request
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.request.receive

fun Route.userRoutes(userService: UserService) {
    route("/users") {
        get("/getById/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val user = id?.let { userService.getById(it) }
            if (user != null) {
                call.respond(user)
            } else {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            }
        }
        get("/getByUsername/{username}") {
            val username = call.parameters["username"]
            if (username == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "username parameter missing"))
                return@get
            }
            val user = username?.let { userService.getByUsername(it) }
            if (user != null) {
                call.respond(user)
            } else {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            }
        }
        get("/get/{username}") {
            val username = call.parameters["username"]
            if (username == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "username parameter missing"))
                return@get
            }
            val user = username?.let { userService.getByUsername(it) }
            if (user != null) {
                call.respond(user)
            } else {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            }
        }
        authenticate ("auth-jwt"){
            get("/private/me"){
                call.configureExceptionHandling {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    if (principal != null) {
                        val userId = principal.payload.getClaim("userId").asInt()
                        val user = userId?.let { userService.getById(it) }
                        if (user != null) {
                            call.respond(user)
                        } else {
                            call.respondText("User not found", status = HttpStatusCode.NotFound)
                        }
                    }
                }
            }
        }
        authenticate("auth-jwt") {
            put("/edit-user/{id}") {
                call.configureExceptionHandling {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val idParam = call.parameters["id"]?.toIntOrNull()
                    principal?.payload?.getClaim("userId")?.asInt().let {
                        if (it == idParam){
                            val request =call.receive<UpdateUserRequest>()
                            val result = userService.updateUser(idParam!! ,request )
                            call.respond(HttpStatusCode.OK, result)
                        }else{
                            throw UnauthorizedException()
                        }
                    }
                }

            }

        }

    }
}
