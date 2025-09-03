package com.example.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

import com.example.config.AppConfig
import java.util.*
import com.auth0.jwt.JWTVerifier

class JWTConfig(cfg: AppConfig.Jwt) {
    val issuer: String = cfg.issuer
    val audience: String = cfg.audience
    val realm: String = cfg.realm
    val expiresInMs: Long = cfg.expiresInSec * 1000
    val algorithm: Algorithm = Algorithm.HMAC256(cfg.secret)

    val verifier: JWTVerifier by lazy {
        JWT.require(algorithm)
            .withIssuer(issuer)
            .withAudience(audience)
            .build()
    }

    fun generateToken(userId: Int, username: String): String =
        JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(username)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + expiresInMs))
            .sign(algorithm)
}



