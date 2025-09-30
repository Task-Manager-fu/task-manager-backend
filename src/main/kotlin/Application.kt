package com.example

import com.example.Task.TaskRepository
import com.example.Task.TaskService
import com.example.config.AppConfig
import com.example.plugins.configureDatabase
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.auth.JWTConfig
import com.example.auth.AuthService
import com.example.comment.CommentRepository
import com.example.comment.CommentService
import com.example.user.UserRepository
import com.example.user.UserService
import com.example.exceptions.configureExceptionHandling
import com.example.models.LoginRequest
import com.example.team.TeamRepository
import com.example.team.TeamService
import com.example.utils.configureSwagger
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.receiveText
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json


fun main() {
    io.ktor.server.cio.EngineMain.main(emptyArray())
}

fun Application.module() {
    val appConfig = AppConfig.from(environment.config)
    val jwtConfig = JWTConfig(appConfig.jwt)
    configureSecurity(jwtConfig)
    configureSerialization()
    configureDatabase(appConfig)

    val userRepository = UserRepository()
    val userService = UserService(userRepository)
    val authService = AuthService(userService, jwtConfig)
    val taskService = TaskService(TaskRepository(), TeamRepository())
    val commentService = CommentService(CommentRepository() , TeamRepository())
    configureRouting(
        authService,
        userService ,
        TeamService(TeamRepository()),
        taskService,
        commentService
    )

}
