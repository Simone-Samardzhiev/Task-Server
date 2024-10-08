package com.example.task.service

import com.example.task.model.Task
import com.example.task.model.TaskWithoutId
import com.example.task.repository.TaskRepositoryInterface
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID

/**
 * Interface used to create task service.
 * @property taskRepository The task repository.
 */
interface TaskServiceInterface {
    val taskRepository: TaskRepositoryInterface

    /**
     * Method that will get all tasks that belongs to a user.
     * @param principal The JWT principal.
     * @return List of the task that where retrieved.
     */
    suspend fun getTasks(principal: JWTPrincipal): List<Task>

    /**
     * Method that will add a new task.
     * @param task The task that will be added.
     * @param principal The JWT principal.
     */
    suspend fun addTask(task: TaskWithoutId, principal: JWTPrincipal)

    /**
     * Method that will update an existing task.
     * @param task The task that will be updated.
     */
    suspend fun updateTask(task: Task)

    /**
     * Method that will delete an existing task.
     * @param task The task that will be deleted.
     */
    suspend fun deleteTask(task: Task)
}