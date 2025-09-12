package com.example.team

import com.example.user.User
import kotlinx.serialization.Serializable
@Serializable
data class TeamEntity(
    val id: Int? = null,
    val name: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val description: String? = null,
    val creatorId: Int? = null,
)

@Serializable
data class TeamDTO(
    val id: Int? = null,
    val name: String? = null,
    val userIds: List<Int> = emptyList(),
    val users: List<User>? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val creatorId: Int? = null,
    val description: String? = null,
)