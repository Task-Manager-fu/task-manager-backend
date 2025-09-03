package com.example.user

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
class UserRepository {

    private fun rowToUser(row: ResultRow) = User(
        id = row[UsersTable.id],
        username = row[UsersTable.username],
        email = row[UsersTable.email],
        passwordHash = row[UsersTable.passwordHash],
        createdAtMillis = row[UsersTable.createdAt].toEpochMilli(),
        phoneNumber = row[UsersTable.phoneNumber],
        avatar = row[UsersTable.avatar],
        bio = row[UsersTable.bio],
        role = row[UsersTable.role],
        isActive = row[UsersTable.isActive]
    )

    suspend fun create(username: String, email: String, passwordHash: String): User =
        newSuspendedTransaction {
            val insertStatement = UsersTable.insert {
                it[UsersTable.username] = username
                it[UsersTable.email] = email
                it[UsersTable.passwordHash] = passwordHash
            }

            val id = insertStatement[UsersTable.id]

            val query = UsersTable
                .select(UsersTable.columns)
                .where { UsersTable.id eq id }

            query.singleOrNull()?.let(::rowToUser) as User
        }


    suspend fun findByEmail(email: String): User? = newSuspendedTransaction {
        val query = UsersTable
            .select(UsersTable.columns)
            .where { UsersTable.email eq email }

        query.singleOrNull()?.let(::rowToUser)

    }


    suspend fun findById(id: Int): User? = newSuspendedTransaction {
        UsersTable
            .select(UsersTable.columns)
            .where { UsersTable.id eq id }
            .singleOrNull()?.let(::rowToUser)
    }

    suspend fun findByUsername(username: String): User? = newSuspendedTransaction {
        UsersTable
            .select(UsersTable.columns)
            .where { UsersTable.username.lowerCase() eq username.lowercase() }
            .singleOrNull()?.let(::rowToUser)
    }
    suspend fun existsByEmail(email: String): Boolean = newSuspendedTransaction {
        UsersTable.select(UsersTable.columns)
            .where { UsersTable.email eq email }.limit(1).any()
    }

    suspend fun existsByUsername(username: String): Boolean = newSuspendedTransaction {
        UsersTable.select(UsersTable.columns).where { UsersTable.username eq username }.limit(1).any()
    }
}
