package com.example.task.model.repository

import com.example.task.model.Task
import java.sql.SQLException
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
}