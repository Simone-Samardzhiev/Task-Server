package com.example.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.user.UserRepository
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.Date
import java.util.UUID

// Service used to create JWT tokens
object JWTUserService : JWTServiceInterface {
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

    // Method used to check the JWT credentials
    override suspend fun validateCredentials(credentials: JWTCredential): JWTPrincipal? {
        return if (credentials.payload.audience.contains(audience) && credentials.payload.issuer.equals(issuer)) {
            val claim = credentials.payload.getClaim("id").asString()
            val id = UUID.fromString(claim)

            return if (UserRepository.checkUserId(id)) {
                JWTPrincipal(credentials.payload)
            } else {
                null
            }
        } else {
            null
        }
    }
}