package com.example.task.services

import com.example.task.errors.TaskIdNotFoundError
import com.example.task.models.Task
import com.example.task.models.NewTask
import com.example.task.repositories.TaskRepositoryInterface
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID


class TaskService(override val taskRepository: TaskRepositoryInterface) : TaskServiceInterface {
    override suspend fun getTasks(principal: JWTPrincipal): List<Task> {
        val id = UUID.fromString(principal.payload.getClaim("id").asString())
        return taskRepository.getTasksByUserId(id)
    }
    override suspend fun addTask(task: NewTask, principal: JWTPrincipal) {
        val id = UUID.fromString(principal.payload.getClaim("id").asString())
        val task = Task(
            id = UUID.randomUUID(),
            name = task.name,
            description = task.description,
            priority = task.priority,
            dueDate = task.dueDate,
            dateDeleted = null,
            dateCompleted = null
        )

        taskRepository.addTask(task, id)
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