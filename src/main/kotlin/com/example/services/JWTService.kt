package com.example.services

import com.auth0.jwt.JWT
import org.h2.engine.User

object JWTService {
    val secret = System.getenv("secret") ?: throw IllegalStateException("Missing secret!")
    const val audience = "Task-App-Client"
    const val issuer = "http://localhost:8080"
    const val realm = "Task-App"
}