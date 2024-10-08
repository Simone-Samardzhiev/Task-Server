package com.example.task.repository

import com.example.task.model.Priority
import com.example.task.model.Task
import com.example.task.table.TaskTable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TaskRepository : TaskRepositoryInterface {
    override suspend fun getTasksByUserId(userId: UUID): List<Task> {
        return transaction {
            TaskTable.selectAll()
                .where { TaskTable.userId eq userId }
                .map {
                    Task(
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
                it[TaskTable.userId] = userId
            }
        }
    }

    override suspend fun updateTask(task: Task): Boolean {
        return transaction {
            val updatedRows = TaskTable
                .update({ TaskTable.id eq task.id }) {
                    it[name] = task.name
                    it[description] = task.description
                    it[priority] = task.priority.name
                    it[dueDate] = task.dueDate
                    it[dateDeleted] = task.dateDeleted
                    it[dateCompleted] = task.dateCompleted
                }

            updatedRows == 1
        }
    }

    override suspend fun deleteTask(task: Task): Boolean {
        return transaction {
            val deletedRows = TaskTable
                .deleteWhere {
                    id eq task.id
                }

            deletedRows == 1
        }
    }
}