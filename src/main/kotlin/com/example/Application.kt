package com.example


import com.example.plugins.configureDatabase
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import com.example.user.jwt.JWTUserService
import com.example.user.repository.UserRepository
import com.example.user.service.UserService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val userRepository = UserRepository()
    val jwtUserService = JWTUserService(userRepository)
    val userService = UserService(userRepository, jwtUserService)
    configureSerialization()
    configureDatabase()
    configureRouting(userService)
}
