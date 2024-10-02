package com.example.user.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

// Table used to get data about users
object UserTable : Table("users") {
    val id: Column<UUID> = uuid("id")
    val email: Column<String> = varchar("email", 255).uniqueIndex()
    val password: Column<String> = varchar("password", 255)
    override val primaryKey = PrimaryKey(id)
}