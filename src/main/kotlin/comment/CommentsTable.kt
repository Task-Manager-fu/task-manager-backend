package com.example.comment

import com.example.Task.TasksTable
import com.example.user.UsersTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object CommentsTable: Table("comment") {
    val id = integer("id").autoIncrement().uniqueIndex()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val userId = integer("userId")
    val content = varchar("content", 600)
    val taskId = reference("taskId", TasksTable.id)
    val username = varchar("username", 255)
    override val primaryKey = PrimaryKey(id)
}