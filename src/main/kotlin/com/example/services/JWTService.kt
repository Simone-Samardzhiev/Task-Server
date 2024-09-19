package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.repositories.UserRepository
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
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

    // Method that will validate the credentials of the token
    fun validateCredentials(credential: JWTCredential): JWTPrincipal? {
        if (credential.payload.audience.contains(audience)) {
            val claim = credential.payload.getClaim("id").asString()
            val id = UUID.fromString(claim)

            if (UserRepository.checkIfIdExists(id)) {
                return JWTPrincipal(credential.payload)
            } else {
                return null
            }
        } else {
            return null
        }
    }
}