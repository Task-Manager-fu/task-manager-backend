package com.example.auth

import kotlinx.serialization.Serializable

@Serializable
data class RequestResetPasswordDTO(val email: String)

@Serializable
data class ResetPasswordDTO(val token: String, val newPassword: String)

