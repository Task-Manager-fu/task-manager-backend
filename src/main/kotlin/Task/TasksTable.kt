package com.example.Task

import com.example.team.TeamsTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.timestamp

object TasksTable: IntIdTable("task") {
    val teamId = reference("team_id" , TeamsTable.id)
    val title = varchar("title", 255)
    val description = varchar("description", 255)
    val deadline = datetime("deadline").nullable()
    val priority = integer("priority")
    val status = enumeration("status", TaskStatus::class).nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)



}