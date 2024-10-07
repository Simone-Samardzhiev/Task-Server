package com.example.user.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.user.repository.UserRepositoryInterface
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.Date
import java.util.UUID

class JWTUserService(
    override val userRepository: UserRepositoryInterface,
) : JWTUserServiceInterface {
    override val secret = System.getenv("secret") ?: throw RuntimeException("Missing secret!")

    override val audience = "Task-App-Client"

    override val issuer: String = "http://localhost:8080"

    override val realm: String = "Task-App"

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

    override suspend fun validateCredentials(credentials: JWTCredential): JWTPrincipal? {
        return if (credentials.payload.audience.contains(audience) && credentials.payload.issuer.equals(issuer)) {
            val claim = credentials.payload.getClaim("id").asString()
            val id = UUID.fromString(claim)

            return if (userRepository.checkUserId(id)) {
                JWTPrincipal(credentials.payload)
            } else {
                null
            }
        } else {
            null
        }
    }
}