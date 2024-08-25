package com.example.tables


import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

// Table used to manage the tasks.
object TaskTable : Table("tasks") {
    // The id of the task.
    val id: Column<UUID> = uuid("id")
    // The name of the task.
    val name: Column<String> = varchar("name", 255)
    // The description of the task.
    val description: Column<String> = varchar("description", 255)
    // Foreign key that reference a user id.
    val userId :Column<UUID> = reference("user_id", UserTable.id)
    // Setting the primary key of the task.
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}