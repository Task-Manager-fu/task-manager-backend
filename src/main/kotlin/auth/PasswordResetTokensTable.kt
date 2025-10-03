package com.example.auth

import com.example.user.UsersTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object PasswordResetTokensTable : IntIdTable("password_reset_tokens") {
    val userId = reference(name = "user_id", refColumn =  UsersTable.id)
    val token = varchar("token", 255).uniqueIndex()
    val expiresAt = datetime("expires_at")
}
