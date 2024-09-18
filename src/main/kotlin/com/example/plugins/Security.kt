package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.services.JWTService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt {
            realm = JWTService.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(JWTService.secret))
                    .withAudience(JWTService.audience)
                    .withIssuer(JWTService.issuer)
                    .build()
            )
            validate { credentials ->
                if (credentials.payload.audience.contains(JWTService.audience)) {
                    JWTPrincipal(credentials.payload)
                } else {
                    null
                }
            }
        }
    }
}