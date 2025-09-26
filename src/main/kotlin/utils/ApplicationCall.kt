package com.example.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend inline fun <reified T> ApplicationCall.respondSuccess(
    status: HttpStatusCode = HttpStatusCode.OK,
    message: String = "Success",
    data: T
) {
    respond(
        status,
        Response(
            status = status.value,
            message = message,
            data = data
        )
    )
}
suspend fun ApplicationCall.respondSuccessMessage(
    status: HttpStatusCode = HttpStatusCode.OK,
    message: String
) {
    respond(
        status,
        Response<Unit>(
            status = status.value,
            message = message
        )
    )
}

