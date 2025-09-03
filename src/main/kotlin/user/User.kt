package com.example.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val username: String,
    val email: String,
    val passwordHash: String,
    val createdAtMillis: Long,
    val phoneNumber: String? = null,
    val role: UserRole? = null,
    val isActive: Boolean = true,
    val avatar: String? = null,
    val bio: String? = null,
    var token: String? = null,
    )
