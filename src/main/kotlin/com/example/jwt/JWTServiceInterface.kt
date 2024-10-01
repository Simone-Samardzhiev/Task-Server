package com.example.jwt

import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID

// Interface used to create JWT service
interface JWTServiceInterface {
    // The secret used to hash the token
    val secret: String

    // The audience that will use the token
    val audience: String

    // The issuer who created the token
    val issuer: String

    // The realm that stores the scope of authentication
    val realm: String

    // Method used to create token
    suspend fun generateToken(userId: UUID): String

    // Method used to check the JWT credentials
    suspend fun validateCredentials(credentials: JWTCredential): JWTPrincipal?
}