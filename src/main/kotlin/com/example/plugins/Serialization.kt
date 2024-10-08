package com.example.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

/**
 * Extension function that will set the content negotiation.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}