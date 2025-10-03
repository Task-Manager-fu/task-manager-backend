package com.example.auth

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class PasswordResetRepository {

    suspend fun createToken(userId: Int, token: String, expiresAt: LocalDateTime) =
        newSuspendedTransaction(Dispatchers.IO) {
            PasswordResetTokensTable.insert {
                it[PasswordResetTokensTable.userId] = userId
                it[PasswordResetTokensTable.token] = token
                it[PasswordResetTokensTable.expiresAt] = expiresAt
            }
        }

    suspend fun getToken(token: String) = newSuspendedTransaction(Dispatchers.IO) {
        PasswordResetTokensTable.selectAll()
            .where{ PasswordResetTokensTable.token eq token }
            .singleOrNull()?.let {
                PasswordResetTokenEntity(
                    id = it[PasswordResetTokensTable.id].value,
                    userId = it[PasswordResetTokensTable.userId],
                    token = it[PasswordResetTokensTable.token],
                    expiresAt = it[PasswordResetTokensTable.expiresAt]
                )
            }
    }

    suspend fun deleteToken(id: Int) = newSuspendedTransaction(Dispatchers.IO) {
        PasswordResetTokensTable.deleteWhere { PasswordResetTokensTable.id eq id }
    }
}

data class PasswordResetTokenEntity(
    val id: Int,
    val userId: Int,
    val token: String,
    val expiresAt: LocalDateTime
)
