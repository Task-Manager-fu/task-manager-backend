package com.example.exceptions

import io.ktor.http.*

open class AppException(
    val status: HttpStatusCode,
    override val message: String,
    val code: String
) : RuntimeException(message)

class BadRequestException(message: String, code: String = "BAD_REQUEST") :
    AppException(HttpStatusCode.BadRequest, message, code)
class EmailExistException(message: String, code: Int = HttpStatusCode.Conflict.value) :
    AppException(HttpStatusCode.Conflict, message, code.toString())
class InvalidInputException(message: String, code: Int = HttpStatusCode.BadRequest.value) :
        AppException(HttpStatusCode.BadRequest, message, code.toString())