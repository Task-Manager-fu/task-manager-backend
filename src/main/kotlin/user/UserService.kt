package com.example.user

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import org.mindrot.jbcrypt.BCrypt

class UserService(private val repo: UserRepository) {

    suspend fun create(username: String, email: String, passwordHash: String): User =
        repo.create(username, email, passwordHash)

    suspend fun updateUser(id:Int ,user: UpdateUserRequest): User =
        repo.updateUser(id , user)
    suspend fun findByEmail(email: String): User? = repo.findByEmail(email)
    suspend fun getById(id: Int): User =
        repo.findById(id) ?: throw NoSuchElementException("User not found")
    suspend fun getByUsername(username: String): User ?=
        repo.findByUsername(username) ?: throw NoSuchElementException("User not found")
    suspend fun existsByEmail(email: String) = repo.existsByEmail(email)
    suspend fun existsByUsername(username: String) = repo.existsByUsername(username)
    suspend fun updatePassword(userId: Int, newPassword: String) {
        val hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt())
        newSuspendedTransaction {
            UsersTable.update({ UsersTable.id eq userId }) {
                it[passwordHash] = hashedPassword
            }
        }
    }


}
