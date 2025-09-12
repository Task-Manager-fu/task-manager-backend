package com.example.team

import com.example.user.User
import com.example.user.UsersTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TeamsUsersTable : Table("team_users") {
    val teamId = reference("team_id", TeamsTable.id ,onDelete = ReferenceOption.CASCADE)
    val userId = reference("user_id", UsersTable.id , onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(teamId, userId)
}
