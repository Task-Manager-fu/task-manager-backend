package com.example.team

import com.example.team.TeamsUsersTable.userId
import com.example.user.User
import com.example.user.UsersTable
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.TripleSerializer
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.time.Instant.now
import kotlin.let

class TeamRepository {
    private fun rowToTeamEntity(row: ResultRow): TeamEntity =
        TeamEntity(
            id = row[TeamsTable.id],
            name = row[TeamsTable.name],
            description = row[TeamsTable.description],
            createdAt = row[TeamsTable.createdAt].toEpochMilli(),
            updatedAt = row[TeamsTable.updatedAt].toEpochMilli(),
            creatorId = row[TeamsTable.creatorId]
        )

    suspend fun createTeam(name: String, description: String? , userIds: List<Int> , creatorId: Int): TeamEntity =
        newSuspendedTransaction(Dispatchers.IO) {
        val insertStatement = TeamsTable.insert {
            it[TeamsTable.name] = name
            it[TeamsTable.description] = description
            it[TeamsTable.createdAt] = now()
            it[TeamsTable.updatedAt] = now()
            it[TeamsTable.creatorId] = creatorId
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
    suspend fun getTeamById(teamId: Int): TeamDTO? = newSuspendedTransaction(Dispatchers.IO) {
        val teamRow = TeamsTable.selectAll().where{ TeamsTable.id eq teamId }.singleOrNull() ?: return@newSuspendedTransaction null
        val users = (TeamsUsersTable innerJoin UsersTable)
            .selectAll().where { TeamsUsersTable.teamId eq teamId }
            .map { row ->
                User(
                    id = row[UsersTable.id],
                    username = row[UsersTable.username],
                    email = row[UsersTable.email],
                    passwordHash = row[UsersTable.passwordHash],
                    createdAtMillis = row[UsersTable.createdAt].toEpochMilli(),
                    phoneNumber = row[UsersTable.phoneNumber],
                    role = row[UsersTable.role],
                    isActive = row[UsersTable.isActive],
                    avatar = row[UsersTable.avatar],
                    bio = row[UsersTable.bio]
                )
            }
        val userIds = users.map(User::id)
        TeamDTO(
            id = teamRow[TeamsTable.id],
            name = teamRow[TeamsTable.name],
            description = teamRow[TeamsTable.description],
            createdAt = teamRow[TeamsTable.createdAt].toEpochMilli(),
            updatedAt = teamRow[TeamsTable.updatedAt].toEpochMilli(),
            creatorId = teamRow[TeamsTable.creatorId],
            userIds = userIds as List<Int>,
            users = users
        )
    }
    suspend fun getTeamsByUser(userId: Int, page: Int = 1, pageSize: Int = 10): List<TeamEntity> =
        newSuspendedTransaction(Dispatchers.IO) {
            val allTeams = (TeamsTable innerJoin TeamsUsersTable)
                .selectAll()
                .where { TeamsUsersTable.userId eq userId }
                .map(::rowToTeamEntity)
            val fromIndex = (pageSize - 1) * page
            val toIndex = (fromIndex + page).coerceAtMost(allTeams.size)
            if (fromIndex >= allTeams.size) emptyList()
            else allTeams.subList(fromIndex, toIndex)
        }
    suspend fun addUserToTeam(teamId: Int, userIds: List<Int>): Boolean =
        newSuspendedTransaction(Dispatchers.IO) {
            userIds.forEach { userId ->
                TeamsUsersTable.insertIgnore {
                    it[TeamsUsersTable.teamId] = teamId
                    it[TeamsUsersTable.userId] = userId
                }
            }
            return@newSuspendedTransaction true
    }
    suspend fun removeUserFromTeam(teamId: Int , userId: Int): Boolean {
        newSuspendedTransaction(context = Dispatchers.IO) {
            TeamsUsersTable.deleteWhere {
                (TeamsUsersTable.teamId eq teamId) and
                        (TeamsUsersTable.userId eq userId)
            }
        }
        return true
    }
    suspend fun updateTeam(teamId: Int
                           , name: String
                           , description: String): TeamEntity
    =
        newSuspendedTransaction(Dispatchers.IO) {
            TeamsTable.update({ TeamsTable.id eq teamId }) {
               it[TeamsTable.name] = name
                it[TeamsTable.description] = description
                it[TeamsTable.updatedAt] = now()
            }
            TeamsTable.selectAll()
                .where { TeamsTable.id eq teamId }
                .single()
                .let(::rowToTeamEntity)
        }

    suspend fun deleteTeam(teamId: Int, userIds: List<Int>): Boolean {
        newSuspendedTransaction(context = Dispatchers.IO) {
            userIds.forEach { userId ->
                TeamsUsersTable.deleteWhere {
                    (TeamsUsersTable.teamId eq teamId) and
                            (TeamsUsersTable.userId eq userId)
                }
            }
            TeamsTable.deleteWhere{
                TeamsTable.id eq teamId
            }
        }
        return true
    }

}