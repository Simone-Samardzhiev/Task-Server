package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.user.jwt.JWTUserServiceInterface
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

/**
 * Extension function used to configure the jwt authentication if the server.
 */
fun Application.configureSecurity(jwtUserService: JWTUserServiceInterface) {
    install(Authentication) {
        jwt {
            realm = jwtUserService.realm // Setting the realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtUserService.secret)) // Setting the hash algorithm
                    .withAudience(jwtUserService.audience) // Setting the audience
                    .withIssuer(jwtUserService.issuer) // Setting the issuer
                    .build() // Building
            )
            validate { credentials ->
                jwtUserService.validateCredentials(credentials) // Validation the credentials
            }
        }
    }
}