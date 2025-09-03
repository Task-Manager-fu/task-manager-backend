package com.example.utils

import io.ktor.server.application.*
import io.ktor.server.request.*

suspend inline fun <reified T : Any> ApplicationCall.receiveOrFail(): T =
    try { receive<T>() }
    catch (e: Exception) {
        throw IllegalArgumentException("Invalid body")
    }
