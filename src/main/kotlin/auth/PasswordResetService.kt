package com.example.auth

import com.example.user.UserService
import java.time.LocalDateTime
import java.util.UUID

class PasswordResetService(
    private val userService: UserService,
    private val resetRepository: PasswordResetRepository,
    private val emailService: EmailService
) {

    suspend fun requestReset(email: String) {
        val user = userService.findByEmail(email)
            ?: return

        val token = UUID.randomUUID().toString()
        val expiresAt = LocalDateTime.now().plusMinutes(5)

        resetRepository.createToken(user.id!!, token, expiresAt)
        emailService.sendResetPasswordEmail(user.email, token)
    }

    suspend fun resetPassword(token: String, newPassword: String) {
        val tokenEntity = resetRepository.getToken(token)
            ?: throw IllegalArgumentException("Invalid token")

        if(tokenEntity.expiresAt.isBefore(LocalDateTime.now()))
            throw IllegalArgumentException("Token expired")

        userService.updatePassword(tokenEntity.userId, newPassword)
        resetRepository.deleteToken(tokenEntity.id)
    }
}
