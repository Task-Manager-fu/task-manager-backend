package com.example.comment

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Int? = null,
    val userId: Int? = null,
    val taskId: Int? = null,
    val content: String? = null,
    val createdAt: Long? = null,
    val username: String?=null,
    var isDeletable: Boolean?=null,
)