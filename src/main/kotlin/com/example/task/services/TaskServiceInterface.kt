package com.example.task.services

import com.example.task.models.Task
import com.example.task.models.NewTask
import com.example.task.repositories.TaskRepositoryInterface
import io.ktor.server.auth.jwt.JWTPrincipal

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
    suspend fun addTask(task: NewTask, principal: JWTPrincipal)

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