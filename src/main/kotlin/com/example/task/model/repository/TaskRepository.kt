package com.example.task.model.repository

import com.example.task.model.Priority
import com.example.task.model.Task
import com.example.task.table.TaskTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class TaskRepository: TaskRepositoryInterface {
    override suspend fun getTasksByUserId(userId: UUID): List<Task> {
        return transaction {
            TaskTable.selectAll()
                .where { TaskTable.userId eq userId }
                .map {
                    Task (
                        id = it[TaskTable.id],
                        name = it[TaskTable.name],
                        description = it[TaskTable.description],
                        priority = Priority.valueOf(it[TaskTable.priority]),
                        dueDate = it[TaskTable.dueDate],
                        dateDeleted = it[TaskTable.dateDeleted],
                        dateCompleted = it[TaskTable.dateCompleted]
                    )
                }
        }
    }

    override suspend fun addTask(task: Task, userId: UUID) {
        transaction {
            TaskTable.insert {
                it[id] = task.id
                it[name] = task.name
                it[description] = task.description
                it[priority] = task.priority.name
                it[dueDate] = task.dueDate
                it[dateDeleted] = task.dateDeleted
                it[dateCompleted] = task.dateCompleted
            }
        }
    }
}