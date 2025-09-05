package com.example.models

import com.example.user.User
import com.example.user.UserRole
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
@Serializable
data class UserResponse(
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
    val token: String? = null,
) {
    companion object {
        fun from(u: User) = UserResponse(
            id = u.id!!,
            username = u.username,
            email = u.email,
            createdAtMillis = u.createdAtMillis,
            phoneNumber = u.phoneNumber,
            role = u.role,
            isActive = u.isActive,
            avatar = u.avatar,
            bio = u.bio,
            passwordHash = u.passwordHash!!,
            token = u.token
        )
    }
}
