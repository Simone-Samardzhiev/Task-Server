package com.example.task.table

import com.example.user.table.UserTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

/**
 * Object used to manage the tasks table.
 * @property id The primary key of the table that is UUID.
 * @property name The name of the task.
 * @property description The description of the task.
 * @property priority The priority of the task.
 * @property dueDate The date till the task has to be completed.
 * @property dateCompleted The date when the task was completed.
 * @property dateDeleted The date when the task was deleted.
 * @property userId The id of the user that the task belongs to.
 */
object TaskTable : Table("tasks") {
    val id: Column<UUID> = uuid("id")
    val name: Column<String> = varchar("name", 50)
    val description: Column<String> = varchar("description", 50)
    val priority: Column<String> = varchar("priority", 50)
    val dueDate: Column<LocalDateTime> = datetime("due_date")
    val dateCompleted: Column<LocalDate?> = date("date_completed").nullable()
    val dateDeleted: Column<LocalDate?> = date("date_deleted").nullable()
    val userId: Column<UUID> = reference("user_id", UserTable.id)
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
