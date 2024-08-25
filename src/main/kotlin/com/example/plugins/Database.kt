package com.example.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database


fun Application.configureDatabase() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/tasks_database",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = ""
    )
}