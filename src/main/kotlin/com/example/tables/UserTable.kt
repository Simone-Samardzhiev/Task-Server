package com.example.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

// Table used to manage users.
object UserTable: Table("users") {
    // The id of the user.
    val id: Column<UUID> = uuid("id")
    // The email of the user.
    val email: Column<String> = varchar("email", 255)
    // The password of the user.
    val password: Column<String> = varchar("password", 255)
    // Setting the primary key of the user.
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}