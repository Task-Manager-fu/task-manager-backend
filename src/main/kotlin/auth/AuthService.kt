package com.example.auth
import com.example.user.UserService
import com.example.utils.HashUtil
import com.example.exceptions.BadRequestException
import com.example.exceptions.EmailExistException
import com.example.models.LoginRequest
import com.example.models.RegisterRequest
import com.example.models.TokenResponse
import com.example.models.UserResponse
import com.example.utils.Utility

class AuthService(
    private val userService: UserService,
    private val jwt: JWTConfig
) {
    suspend fun register(req: RegisterRequest): UserResponse {
        // Validation
//        Utility.Validator.validateRegistration(req.email , req.password)
        if (userService.existsByEmail(req.email)) throw EmailExistException("Email already used")
        if (userService.existsByUsername(req.username)) throw BadRequestException("Username already used")

        val hash = HashUtil.hash(req.password)
        val user = userService.create(req.username, req.email, hash)
        user.token  = jwt.generateToken(user.id!!, user.username)
        return UserResponse.from(user)
    }

    suspend fun login(req: LoginRequest): TokenResponse {
        val user = userService.findByEmail(req.email)
            ?: throw BadRequestException("Invalid credentials")

        if (!HashUtil.verify(req.password, user.passwordHash!!))
            throw BadRequestException("Invalid credentials")

        val token = jwt.generateToken(user.id!!, user.username)
        return TokenResponse(token = token, tokenType = "Bearer", expiresIn = jwt.expiresInMs / 1000)
    }

}
