package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    val secret = System.getenv("secret") ?: throw Exception("Missing secret!")
    val audience = "Task-App-Client"
    val issuer = "http://localhost:8080"

    install(Authentication) {
        jwt {
            realm = "Task-App"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credentials ->
                if (credentials.payload.audience.contains(audience)) {
                    JWTPrincipal(credentials.payload)
                } else {
                    null
                }
            }
        }
    }
}