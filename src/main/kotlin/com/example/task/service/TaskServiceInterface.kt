package com.example.task.service

import com.example.task.model.Task
import com.example.task.model.TaskWithoutId
import com.example.task.repository.TaskRepositoryInterface
import com.example.user.repository.UserRepositoryInterface
import java.util.UUID

/**
 * Interface used to create task service.
 * @property taskRepository The task repository.
 */
interface TaskServiceInterface {
    val taskRepository: TaskRepositoryInterface

    /**
     * Method that will get all tasks that belongs to a user.
     * @param userId The user id.
     * @return List of the task that where retrieved.
     */
    suspend fun getTasks(userId: UUID): List<Task>

    /**
     * Method that will add a new task.
     * @param task The task that will be added.
     */
    suspend fun addTask(task: TaskWithoutId, userId: UUID)
}