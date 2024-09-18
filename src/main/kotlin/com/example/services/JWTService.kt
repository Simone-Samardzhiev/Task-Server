package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date
import java.util.UUID

object JWTService {
    val secret = System.getenv("secret") ?: throw IllegalStateException("Missing secret!")
    const val audience = "Task-App-Client"
    const val issuer = "http://localhost:8080"
    const val realm = "Task-App"

    // Method that will create a token and put the user id as a claim
    fun createToken(id: UUID): String {
        return JWT
            .create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("id", id.toString())
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + 60000 * 5))
            .sign(Algorithm.HMAC256(secret))
    }
}