package com.example.user

class UserService(private val repo: UserRepository) {

    suspend fun create(username: String, email: String, passwordHash: String): User =
        repo.create(username, email, passwordHash)

    suspend fun findByEmail(email: String): User? = repo.findByEmail(email)
    suspend fun getById(id: Int): User =
        repo.findById(id) ?: throw NoSuchElementException("User not found")
    suspend fun getByUsername(username: String): User ?=
        repo.findByUsername(username) ?: throw NoSuchElementException("User not found")
    suspend fun existsByEmail(email: String) = repo.existsByEmail(email)
    suspend fun existsByUsername(username: String) = repo.existsByUsername(username)
}
