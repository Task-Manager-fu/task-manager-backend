package com.example.team

import com.example.exceptions.configureExceptionHandling
import io.ktor.client.plugins.ConnectTimeoutException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.teamRoutes(teamService: TeamService) {
    route("/teams") {
        authenticate("auth-jwt") {

            post("/new") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val creatorId = principal!!.payload.getClaim("userId").asInt()
                    val request = call.receive<TeamDTO>()
                    val newTeam = teamService.createTeam(request.name, request.description, request.users , creatorId)
                    call.respond(HttpStatusCode.Created, newTeam)
                }
            }
        }
    }
}