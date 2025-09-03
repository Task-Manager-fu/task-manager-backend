package com.example.utils

import com.example.exceptions.InvalidInputException

class Utility {
    object Validator {

        private val EMAIL_REGEX = Regex(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$"
        )

        private val PASSWORD_REGEX = Regex(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!]).{8,}\$"
        )

        fun validateEmail(email: String): Boolean {
            return EMAIL_REGEX.matches(email)
        }

        fun validatePassword(password: String): Boolean {
            return PASSWORD_REGEX.matches(password)
        }

        fun validateRegistration(email: String, password: String) {

            if (!validateEmail(email)) {
                throw InvalidInputException("Invalid email format")
            }

            if (!validatePassword(password)) {
                throw InvalidInputException(   "Password must be at least 8 characters and include " +
                        "uppercase, lowercase, number, and special character")

            }

        }
    }

}