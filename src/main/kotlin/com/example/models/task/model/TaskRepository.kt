package com.example.models.task.model

import com.example.models.user.model.User
import com.example.models.user.model.UserRepository
import com.example.tables.TaskTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

// Repository used to get task data from the table.
object TaskRepository {
    // Method that will return all task of a user if the credentials are correct.
    fun getTasks(user: User): List<Task>? {
        return transaction {
            val userId = UserRepository.getUserId(user)
            userId?.let {
                TaskTable
                    .selectAll()
                    .where { TaskTable.user_id eq it }
                    .map {
                        Task(
                            it[TaskTable.id],
                            it[TaskTable.name],
                            it[TaskTable.description],
                            Priority.valueOf(it[TaskTable.priority])
                        )
                    }
            }
        }
    }

    // Method that will add a task if the user credentials are correct.
    fun addTask(user: User, task: TaskWithoutId): Boolean {
        return transaction {
            val userId = UserRepository.getUserId(user)
            userId?.let {
                TaskTable
                    .insert {
                        it[id] = UUID.randomUUID()
                        it[name] = task.name
                        it[description] = task.description
                        it[priority] = task.priority.name
                        it[user_id] = userId
                    }
                true
            } ?: false
        }
    }

}