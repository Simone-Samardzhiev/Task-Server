package com.example.task.service

import com.example.task.error.TaskIdNotFoundError
import com.example.task.model.Task
import com.example.task.model.TaskWithoutId
import com.example.task.repository.TaskRepositoryInterface
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID


class TaskService(override val taskRepository: TaskRepositoryInterface) : TaskServiceInterface {
    override suspend fun getTasks(principal: JWTPrincipal): List<Task> {
        val id = UUID.fromString(principal.payload.getClaim("id").toString())
        return taskRepository.getTasksByUserId(id)
    }
    override suspend fun addTask(task: TaskWithoutId, userId: UUID) {
        val task = Task(
            id = UUID.randomUUID(),
            name = task.name,
            description = task.description,
            priority = task.priority,
            dueDate = task.dueDate,
            dateDeleted = task.dateDeleted,
            dateCompleted = task.dateCompleted
        )

        taskRepository.addTask(task, userId)
    }


    /**
     * Method that will update an existing task.
     * @param task The task that will be updated.
     * @throws TaskIdNotFoundError When the task id couldn't be found.
     */
    override suspend fun updateTask(task: Task) {
        if (!taskRepository.updateTask(task)) {
            throw TaskIdNotFoundError()
        }
    }


    /**
     * Method that will delete an existing task.
     * @param task The task that will be deleted.
     * @throws TaskIdNotFoundError When the task ud couldn't be found.
     */
    override suspend fun deleteTask(task: Task) {
        if (!taskRepository.deleteTask(task)) {
            throw TaskIdNotFoundError()
        }
    }
}