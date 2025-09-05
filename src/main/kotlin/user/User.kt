package com.example.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val username: String,
    val email: String,
    val passwordHash: String? = null,
    val createdAtMillis: Long = 0,
    val phoneNumber: String? = null,
    val role: UserRole? = null,
    val isActive: Boolean = true,
    val avatar: String? = null,
    val bio: String? = null,
    var token: String? = null,
    )
@Serializable
data class UpdateUserRequest(
    val username: String? = null,
    val email: String? = null,
    val passwordHash: String? = null,
    val phoneNumber: String? = null,
    val role: UserRole? = null,
    val isActive: Boolean? = null,
    val avatar: String? = null,
    val bio: String? = null,
)
