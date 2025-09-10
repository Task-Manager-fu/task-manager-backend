package com.example.team

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object TeamsTable: Table("teams") {
    val id = integer(name = "id").autoIncrement().uniqueIndex()
    val name = varchar("name", 255)
    val description = varchar("description", 255).nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(id)
}