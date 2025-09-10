package com.example.team

import com.example.user.User
import com.example.user.UsersTable
import org.jetbrains.exposed.sql.Table

object TeamsUsersTable : Table("team_users") {
    val teamId = reference("team_id", TeamsTable.id)
    val userId = reference("user_id", UsersTable.id)

    override val primaryKey = PrimaryKey(teamId, userId)
}
