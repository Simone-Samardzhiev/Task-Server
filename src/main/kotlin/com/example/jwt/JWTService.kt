package com.example.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date
import java.util.UUID

// Service used to create JWT tokens
object JWTService : JWTServiceInterface {
    // The secret used to hash the token
    override val secret = System.getenv("secret") ?: throw RuntimeException("Missing secret!")

    // The audience that will use the token
    override val audience = "Task-App-Client"

    // The issuer who created the token
    override val issuer: String = "http://localhost:8080"

    // The realm that stores the scope of authentication
    override val realm: String = "Task-App"

    // Method used to create token
    override suspend fun generateToken(userId: UUID): String {
        return JWT
            .create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("id", userId.toString())
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + 60000 * 5))
            .sign(Algorithm.HMAC256(secret))
    }
}