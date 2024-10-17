package com.example.user.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

/**
 * Object used to manage the users table.
 * @param id The id of the user.
 * @param email The email of the user.
 * @param password The password of the user.
 */
object UserTable : Table("users") {
    val id: Column<UUID> = uuid("id")
    val email: Column<String> = varchar("email", 255).uniqueIndex()
    val password: Column<String> = varchar("password", 255)
    override val primaryKey = PrimaryKey(id)
}