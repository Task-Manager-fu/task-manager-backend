package com.example.team

import com.example.user.UsersTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant.now

class TeamRepository {
    private fun rowToTeamEntity(row: ResultRow): TeamEntity =
        TeamEntity(
            id = row[TeamsTable.id],
            name = row[TeamsTable.name],
            description = row[TeamsTable.description],
            createdAt = row[TeamsTable.createdAt].toEpochMilli(),
            updatedAt = row[TeamsTable.updatedAt].toEpochMilli()
        )
    suspend fun createTeam(name: String, description: String? , userIds: List<Int>): TeamEntity =
        newSuspendedTransaction(Dispatchers.IO) {
        val insertStatement = TeamsTable.insert {
            it[TeamsTable.name] = name
            it[TeamsTable.description] = description
            it[TeamsTable.createdAt] = now()
            it[TeamsTable.updatedAt] = now()
        }
            val id = insertStatement[TeamsTable.id]
            userIds.forEach { userId ->
                TeamsUsersTable.insert {
                    it[TeamsUsersTable.teamId] = id
                    it[TeamsUsersTable.userId] = userId
                }
            }
            TeamsTable.select(TeamsTable.columns).where { TeamsTable.id eq id }
                .single().let(::rowToTeamEntity)
    }
}