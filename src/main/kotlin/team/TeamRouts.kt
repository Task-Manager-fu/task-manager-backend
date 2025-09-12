package com.example.team

import com.example.exceptions.AccessDenied
import com.example.exceptions.AppException
import com.example.exceptions.configureExceptionHandling
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.teamRoutes(teamService: TeamService) {
    route("/teams") {
        authenticate("auth-jwt") {
            post("/new") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val creatorId = principal!!.payload.getClaim("userId").asInt()
                    val request = call.receive<TeamDTO>()
                    val newTeam = teamService.createTeam(request.name!!, request.description, request.userIds, creatorId)
                    call.respond(HttpStatusCode.Created, newTeam)
                }
            }
            get("/getById/{id}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val teamId = call.parameters["id"]?.toIntOrNull()
                    val userId = principal!!.payload.getClaim("userId").asInt()
                    val team = teamService.getTeam(teamId!!)
                    if (!team?.users?.any { it.id == userId }!!) {
                        throw AccessDenied()
                    }
                    call.respond(team)
                }
            }
            get("/getAll") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("userId").asInt()
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                    val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
                    val teams = teamService.getAllTeams(userId, page, pageSize)
                    call.respond(teams)
                }
            }
            delete("/delete/{id}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val teamId = call.parameters["id"]?.toIntOrNull()
                    val userId = principal!!.payload.getClaim("userId").asInt()
                    val request = teamService.deleteTeam(teamId!!, userId!!)
                    call.respond(request)
                }
            }
            put("/update/{id}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val teamId = call.parameters["id"]?.toIntOrNull()
                    val userId = principal!!.payload.getClaim("userId").asInt()
                    val request = call.receive<TeamEntity>()
                    val result = teamService.updateTeam(teamId!! , userId , request)
                    call.respond(HttpStatusCode.OK, result)
                }
            }
            post("/add-user/{teamId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val teamId = call.parameters["teamId"]?.toIntOrNull()
                    val userId = principal!!.payload.getClaim("userId").asInt()
                    val request = call.receive<TeamDTO>()
                    val result = teamService.addUserToTeam(teamId!! , request , userId)
                    call.respond(HttpStatusCode.OK, result)

                }
            }
            delete("/remove-user/{teamId}/{userId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val teamId = call.parameters["teamId"]?.toIntOrNull()
                    val userId = call.parameters["userId"]?.toIntOrNull()
                    val creatorId = principal!!.payload.getClaim("userId").asInt()
                    val request = teamService.removeUserFromTeam(teamId!!, userId!! , creatorId)
                    call.respond(HttpStatusCode.OK, request)
                }
            }
        }
    }
}