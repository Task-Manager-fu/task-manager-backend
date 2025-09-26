package com.example.Task

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
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.taskRoutes(service: TaskService) {
    route("/tasks") {
        authenticate("auth-jwt") {
            post("/new") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asInt()
                    val request = call.receive<TaskDTO>()
                    val newTaskDTO = service.addTask(request , userId)
                    call.respondSuccess(HttpStatusCode.Created ,"Successfully added" , newTaskDTO)
                }
            }
            put("/update/{teamId}/{taskId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asInt()
                    val taskId = call.parameters["taskId"]!!
                    val teamId = call.parameters["teamId"]!!
                    val request = call.receive<TaskEntity>()
                    val updateRequest = service.updateTask(request ,userId , teamId.toInt() ,taskId.toInt())
                    call.respondSuccess(HttpStatusCode.OK, "Successfully updated",updateRequest)
                }
            }
            delete("/delete/{teamId}/{taskId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asInt()
                    val taskId = call.parameters["taskId"]!!
                    val teamId = call.parameters["teamId"]!!
                    service.deleteTask(taskId.toInt() , teamId.toInt() , userId)
                    call.respondSuccessMessage(HttpStatusCode.OK, message = "Task deleted successfully!")
                }
            }
            post("/assign/{teamId}/{taskId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asInt()
                    val taskId = call.parameters["taskId"]!!
                    val teamId = call.parameters["teamId"]!!
                    val request = call.receive<TaskDTO>()
                    service.assignTaskToUser(taskId.toInt() , request.assignedToIds!! , userId.toInt(), teamId.toInt())
                    call.respondSuccessMessage(HttpStatusCode.OK, "Successfully assigned!")
                }
            }
            get("/getMyTasks") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asInt()
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                    val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
                    val tasks = service.getMyTasks(userId.toInt() , page = page, pageSize = pageSize)
                    call.respondSuccess(HttpStatusCode.OK, "Successfully retrieved tasks!" , tasks)
                }
            }
            get("/getTasksOfTeam/{teamId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asInt()
                    val teamId = call.parameters["teamId"]!!
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                    val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
                    val result = service.getTasksOfTeam(teamId.toInt() , page , pageSize , userId)
                    call.respondSuccess(HttpStatusCode.OK, "Successfully retrieved tasks!" , result)
                }
            }
            patch("/changeStatus/{teamId}/{taskId}") {
                call.configureExceptionHandling {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("userId").asInt()
                    val taskId = call.parameters["taskId"]!!
                    val teamId = call.parameters["teamId"]!!
                    val request = call.receive<ChangeStatusRequest>()
                    service.changeTaskStatus(teamId.toInt() , taskId.toInt() , request.status , userId)
                    call.respondSuccessMessage(HttpStatusCode.OK, "Successfully changed status!")
                }
            }
        }
    }
}