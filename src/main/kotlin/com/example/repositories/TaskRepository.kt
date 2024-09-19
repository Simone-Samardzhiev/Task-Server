package com.example.repositories

import com.example.models.Priority
import com.example.models.Task
import com.example.tables.TaskTable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

// Object that will manage the data of tasks
object TaskRepository {
    // Method that will return the tasks that belong to a user
    fun getTasks(id: UUID): List<Task> {
        val result = mutableListOf<Task>()
        transaction {
            TaskTable
                .select(TaskTable.id, TaskTable.name, TaskTable.description, TaskTable.priority)
                .where { TaskTable.user_id eq id }
                .map {
                    result.add(
                        Task(
                            id = it[TaskTable.id],
                            name = it[TaskTable.name],
                            description = it[TaskTable.description],
                            priority = Priority.valueOf(it[TaskTable.priority])
                        )
                    )
                }
        }
        return result
    }
}