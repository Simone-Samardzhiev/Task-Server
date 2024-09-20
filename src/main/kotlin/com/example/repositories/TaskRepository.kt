package com.example.repositories

import com.example.models.Priority
import com.example.models.Task
import com.example.models.TaskWithoutId
import com.example.tables.TaskTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

// Object that will manage the data of tasks
object TaskRepository {
    // Method that will return the tasks that belong to a user
    fun getTasks(userId: UUID): List<Task> {
        val result = mutableListOf<Task>()
        transaction {
            TaskTable.select(TaskTable.id, TaskTable.name, TaskTable.description, TaskTable.priority)
                .where { TaskTable.user_id eq userId }
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

    // Method that will add a new task
    fun addTask(task: TaskWithoutId, userId: UUID) {
        transaction {
            TaskTable.insert {
                it[id] = UUID.randomUUID()
                it[name] = task.name
                it[description] = task.description
                it[priority] = task.priority.name
                it[user_id] = userId
            }
        }
    }

    // Method that will delete a task with an id
    fun deleteTask(taskId: UUID): Boolean {
        return transaction {
            val deletedRows = TaskTable.deleteWhere {
                TaskTable.id eq taskId
            }

            deletedRows > 0
        }
    }

    // Method that will update a task information
    fun updateTask(task: Task): Boolean {
        return transaction {
            val updatedRows = TaskTable.update({
                TaskTable.id eq task.id
            }) {
                it[name] = task.name
                it[description] = task.description
                it[priority] = task.priority.name
            }

            updatedRows > 0
        }
    }
}