package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.user.jwt.JWTUserServiceInterface
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(jwtUserService: JWTUserServiceInterface) {
    install(Authentication) {
        jwt {
            realm = jwtUserService.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtUserService.secret))
                    .withAudience(jwtUserService.audience)
                    .withIssuer(jwtUserService.issuer)
                    .build()
            )
            validate { credentials ->
                jwtUserService.validateCredentials(credentials)
            }
        }
    }
}