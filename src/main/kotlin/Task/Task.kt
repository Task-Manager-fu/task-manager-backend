package com.example.Task

import com.example.comment.Comment
import com.example.user.User
import kotlinx.serialization.Serializable

@Serializable
data class TaskEntity(
    val id: Int? = null,
    val teamId: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val status: TaskStatus? = null,
    val deadline: String? = null,
    val priority: Int? = null,
)

@Serializable
data class TaskDTO(
    val id: Int? = null,
    val teamId: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val assignedToIds: MutableList<Int> = mutableListOf(),
    val assignedToUsers: MutableList<User> = mutableListOf(),
    val status: TaskStatus? = null,
    val deadline: String? = null,
    val priority: Int? = null,
    val comments: List<Comment>? = null
)
@Serializable
data class ChangeStatusRequest(
    val status: TaskStatus
)