package com.example.comment

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Int,
    val userId: Int,
    val content: String,
    val createdAt: Long,
    val userName: String?,
)