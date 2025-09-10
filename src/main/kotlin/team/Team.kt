package com.example.team

import com.example.user.User
import kotlinx.serialization.Serializable
@Serializable
data class TeamEntity(
    val id: Int? = null,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val description: String? = null,
)

@Serializable
data class TeamDTO(
    val id: Int? = null,
    val name: String,
    val users: List<Int> = emptyList(),
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val description: String? = null,
)