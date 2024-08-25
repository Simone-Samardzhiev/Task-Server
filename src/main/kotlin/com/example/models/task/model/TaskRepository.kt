package com.example.models.task.model

import com.example.models.user.model.User
import com.example.models.user.model.UserRepository
import com.example.tables.TaskTable
import org.jetbrains.exposed.sql.transactions.transaction

// Repository used to get task data from the table.
object TaskRepository {
    // Method that will return all task of a user if the credentials are correct.
    fun getTasks(user: User): List<Task>? {
        return transaction {
            val id = UserRepository.getUserId(user)
            id?.let {
                TaskTable
                    .select(TaskTable.name, TaskTable.description, TaskTable.priority)
                    .where { TaskTable.userId eq id }
                    .map {
                        Task(it[TaskTable.name], it[TaskTable.description], Priority.valueOf(it[TaskTable.priority]))
                    }
            }
        }
    }
}