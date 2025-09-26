package com.example.exceptions

import com.example.utils.Response
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
        respond(e.status, Response<Unit>(
            status = e.status.value,
            message = e.message,
            errorCode = e.code
        )
        )
    } catch (e: NoSuchElementException) {
        respond(HttpStatusCode.NotFound, Response<Unit>(
            status = HttpStatusCode.NotFound.value,
            message = e.message ?: "Not found",
            errorCode = "NOT_FOUND"
        ))
    } catch (e: Throwable) {
        application.environment.log.error("Unhandled exception", e)
        respond(HttpStatusCode.InternalServerError, Response<Unit>(
            status = HttpStatusCode.InternalServerError.value,
            message = "Internal server error",
            errorCode = "INTERNAL_ERROR"
        ))
    }
}

/**
 * Extension برای استفاده راحت‌تر در routing
 */
fun Route.safeRoute(routeSetup: Route.() -> Unit) {
    routeSetup()
}

