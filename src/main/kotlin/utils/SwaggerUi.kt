package com.example.utils

import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.http.content.defaultResource
import io.ktor.server.http.content.file
import io.ktor.server.http.content.resource
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.openapi.OpenAPIConfig
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
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
        // سرو فایل openapi.json
        get("/openapi.json") {
            val text = this::class.java.classLoader
                .getResource("openapi/openapi.json")!!
                .readText()
            call.respondText(text, ContentType.Application.Json)
        }

        // Swagger UI روی این آدرس
        swaggerUI(path = "swagger", swaggerFile = "/Users/nimo/Downloads/ktor-sample/openapi/openapi.json") {
            version = "4.15.5"
        }
    }
}
