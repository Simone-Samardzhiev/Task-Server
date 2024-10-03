package com.example.plugins

import com.example.user.table.UserTable
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/tasks_database",
        driver = "org.postgresql.Driver",
        user = "simonesamardzhiev",
        password = "Simone_2006"
    )

    transaction {
        SchemaUtils.createMissingTablesAndColumns(UserTable)
    }
}