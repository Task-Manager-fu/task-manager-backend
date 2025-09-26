package com.example.Task

import com.example.user.UsersTable
import org.jetbrains.exposed.sql.Table

object TaskAssignmentsTable: Table("task_assignments") {
    val taskId = reference("task_id", TasksTable.id)
    val userId = reference("user_id", UsersTable.id)

    override val primaryKey = PrimaryKey(taskId, userId)
}