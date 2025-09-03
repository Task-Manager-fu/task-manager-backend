package com.example.user

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp


object UsersTable : Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
    val avatar = varchar("avatar", 255).nullable()
    val bio = varchar("bio", 255).nullable()
    val role = enumerationByName("role", 20, UserRole::class).default(UserRole.USER)
    val isActive = bool("isActive").default(true)
    val phoneNumber = varchar("phoneNumber", 255).nullable()
    val passwordHash = varchar("password_hash", 255)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    override val primaryKey = PrimaryKey(id)
}
