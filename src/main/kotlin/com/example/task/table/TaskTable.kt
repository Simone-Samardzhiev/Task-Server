package com.example.task.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID


object TaskTable : Table("tasks") {
    val id: Column<UUID> = uuid("id")
    val name: Column<String> = varchar("name", 50)
    val description: Column<String> = varchar("description", 50)
    val priority: Column<String> = varchar("priority", 50)
    val dueDate: Column<LocalDateTime> = datetime("due_date")
    val dateCompleted: Column<LocalDate> = date("date_completed")
    val dateDeleted: Column<LocalDate> = date("date_deleted")
    val userId: Column<UUID> = reference("user_id", id).uniqueIndex()
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
