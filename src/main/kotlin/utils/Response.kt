package com.example.utils

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val status: Int,
    val message: String,
    val data: T? = null,
    val errorCode: String? = null
)