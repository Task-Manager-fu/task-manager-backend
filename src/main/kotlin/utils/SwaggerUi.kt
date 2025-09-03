package com.example.utils

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.http.content.file
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.openapi.OpenAPIConfig
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing

fun Application.configureSwagger() {
    routing {
//        openAPI(path = "openapi" ).apply {
//            info = Info().title("My API").version("1.0")
//            components = Components()
//        }
//        openAPI() { // مسیر JSON OpenAPI
//        }
//
//        swaggerUI(path = "swagger", swaggerFile = "openapi.json")
    }
}
