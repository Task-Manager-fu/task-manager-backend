package com.example.comment

import com.example.exceptions.configureExceptionHandling
import com.example.utils.respondSuccess
import com.example.utils.respondSuccessMessage
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.application.call
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.commentRoutes(
    service: CommentService
) {
    route("/comment") {
        authenticate("auth-jwt") {
            post("/add/{teamId}/{taskId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asInt()
                    val username = principal.payload.getClaim("username").asString()
                    val taskId = call.parameters["taskId"]?.toInt()
                    val teamId = call.parameters["teamId"]?.toInt()
                    val request = call.receive<Comment>()
                    val result = service.addComment(userId , username , taskId!! , request.content!! , teamId!!)
                    call.respondSuccess(HttpStatusCode.OK ,"Successfully added."  ,  result)
                }
            }
            get("getComments/{teamId}/{taskId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asInt()
                    val taskId = call.parameters["taskId"]?.toInt()
                    val teamId = call.parameters["teamId"]?.toInt()
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                    val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
                    val result = service.getComments(taskId!! , userId , page, pageSize , teamId!!)
                    call.respondSuccess(HttpStatusCode.OK , "Successfully retrieved comments!" , result)
                }
            }
            delete("delete/{teamId}/{commentId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asInt()
                    val commentId = call.parameters["commentId"]?.toInt()
                    val teamId = call.parameters["teamId"]?.toInt()
                    service.deleteComment(userId , commentId!! , teamId!!)
                    call.respondSuccessMessage(HttpStatusCode.OK , "Successfully deleted." )
                }
            }

        }

    }
}