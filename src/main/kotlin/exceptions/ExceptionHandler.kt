package com.example.exceptions

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.*

/**
 * Extension برای مدیریت exception ها در route ها بدون StatusPages
 */
suspend fun ApplicationCall.configureExceptionHandling(block: suspend () -> Unit) {
    try {
        block()
    } catch (e: AppException) {
        respond(e.status, mapOf("error" to e.message, "code" to e.code))
    } catch (e: NoSuchElementException) {
        respond(HttpStatusCode.NotFound, mapOf("error" to (e.message ?: "Not found")))
    } catch (e: Throwable) {
        application.environment.log.error("Unhandled exception", e)
        respond(HttpStatusCode.InternalServerError, mapOf("error" to "Internal server error"))
    }catch (e: Exception) {
        application.environment.log.error("Unhandled exception", e)
        respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
    }
}

/**
 * Extension برای استفاده راحت‌تر در routing
 */
fun Route.safeRoute(routeSetup: Route.() -> Unit) {
    routeSetup()
}

