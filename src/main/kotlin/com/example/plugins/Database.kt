package com.example.plugins

import com.example.task.tables.TaskTable
import com.example.user.tables.UserTable
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Extension function that will connect to the database and create the missing tables.
 */
fun Application.configureDatabase() {
    // Connecting to the database
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/tasks_database",
        driver = "org.postgresql.Driver",
        user = "simonesamardzhiev",
        password = "Simone_2006"
    )

    // Transaction that will create the missing tables
    transaction {
        SchemaUtils.createMissingTablesAndColumns(UserTable)
        SchemaUtils.createMissingTablesAndColumns(TaskTable)
    }
}