package com.example.auth

import jakarta.mail.Message
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.net.Authenticator
import java.net.PasswordAuthentication
import java.util.Properties

class EmailService(
    private val host: String,
    private val port: Int,
    private val username: String,
    private val password: String,
) {

    private val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", host)
        put("mail.smtp.port", port.toString())
    }

    val authenticator = object : jakarta.mail.Authenticator() {
        override fun getPasswordAuthentication(): jakarta.mail.PasswordAuthentication? {
            return jakarta.mail.PasswordAuthentication(username, password)
        }
    }
    private val session = Session.getInstance(props, authenticator)

    fun sendResetPasswordEmail(to: String, token: String) {
        val message = MimeMessage(session).apply {
            setFrom(InternetAddress("nima77abedini@gmail.com"))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
            subject = "Password Reset Request"
            setText(buildEmailBody(token))
        }

        Transport.send(message)
    }

    private fun buildEmailBody(token: String): String {
        val resetUrl = "https://your-frontend.com/reset-password?token=$token"
        return """
            Hello,
            
            You requested to reset your password.
            
            Click the link below to reset it:
            $resetUrl
            
            If you did not request this, please ignore this email.
            
            This link will expire in 5 minutes.
            
            Best regards,
            Nima Abedini
            
        """.trimIndent()
    }
}
