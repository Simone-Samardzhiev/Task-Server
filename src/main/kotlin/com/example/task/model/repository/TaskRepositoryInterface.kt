package com.example.task.model.repository

import com.example.task.model.Task
import java.util.UUID

/**
 * Interface used to crete task repository.
 */
interface TaskRepositoryInterface {
    /**
     * Method that will return all tasks of a user by the id.
     * @param userId The id of the user.
     * @return Array of the tasks.
     */
    suspend fun getTasksByUserId(userId: UUID): List<Task>

    /**
     * Method that will add a new task.
     * @param task The task that will be added.
     * @param userId The user id who own the task.
     */
    suspend fun addTask(task: Task, userId: UUID)

    /**
     * Method that will update an existing task.
     * @param task The task that will be updated.
     * @return True if a task was updated otherwise false.
     */
    suspend fun updateTask(task: Task): Boolean
}