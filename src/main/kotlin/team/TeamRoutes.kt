package com.example.team

import com.example.exceptions.AccessDeniedCustom
import com.example.exceptions.configureExceptionHandling
import com.example.utils.respondSuccess
import com.example.utils.respondSuccessMessage
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
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
                    call.respondSuccess(HttpStatusCode.Created,"Successfully created" , newTeam)
                }
            }
            get("/getById/{id}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val teamId = call.parameters["id"]?.toIntOrNull()
                    val userId = principal!!.payload.getClaim("userId").asInt()
                    val team = teamService.getTeam(teamId!!)
                    if (!team?.users?.any { it.id == userId }!!) {
                        throw AccessDeniedCustom()
                    }
                    call.respondSuccess(HttpStatusCode.OK , "Successfully" ,team)
                }
            }
            get("/getAll") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("userId").asInt()
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                    val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
                    val teams = teamService.getAllTeams(userId, page, pageSize)
                    call.respondSuccess(HttpStatusCode.OK ,"Successfully",teams)
                }
            }
            delete("/delete/{id}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val teamId = call.parameters["id"]?.toIntOrNull()
                    val userId = principal!!.payload.getClaim("userId").asInt()
                    teamService.deleteTeam(teamId!!, userId!!)
                    call.respondSuccessMessage(HttpStatusCode.OK ,  "Successfully deleted" )
                }
            }
            put("/update/{id}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val teamId = call.parameters["id"]?.toIntOrNull()
                    val userId = principal!!.payload.getClaim("userId").asInt()
                    val request = call.receive<TeamEntity>()
                    val result = teamService.updateTeam(teamId!! , userId , request)
                    call.respondSuccess(HttpStatusCode.OK,"Successfully updated" ,result)
                }
            }
            post("/add-user/{teamId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val teamId = call.parameters["teamId"]?.toIntOrNull()
                    val userId = principal!!.payload.getClaim("userId").asInt()
                    val request = call.receive<TeamDTO>()
                    teamService.addUserToTeam(teamId!! , request , userId)
                    call.respondSuccessMessage(HttpStatusCode.OK, "Successfully added" )

                }
            }
            delete("/remove-user/{teamId}/{userId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()
                    val teamId = call.parameters["teamId"]?.toIntOrNull()
                    val userId = call.parameters["userId"]?.toIntOrNull()
                    val creatorId = principal!!.payload.getClaim("userId").asInt()
                    teamService.removeUserFromTeam(teamId!!, userId!! , creatorId)
                    call.respondSuccessMessage(HttpStatusCode.OK, "Successfully removed" )
                }
            }
        }
    }
}