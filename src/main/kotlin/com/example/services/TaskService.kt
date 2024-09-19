package com.example.services

import com.example.models.ErrorRespond
import com.example.models.Task
import com.example.models.TaskWithoutId
import com.example.repositories.TaskRepository
import io.ktor.http.HttpStatusCode
import java.util.UUID

// Service that will manage the tasks
object TaskService {
    // Method that will get the user tasks from the repository
    fun getTasks(userId: UUID) : List<Task> {
        return TaskRepository.getTasks(userId)
    }

    // Method that will add a task to the repository
    fun addTask(task: TaskWithoutId, userId: UUID) {
        TaskRepository.addTask(task, userId)
    }

    // Method that will delete a task and if the task id is not found
    // it will return ErrorResponse
    fun deleteTask(taskId: UUID): ErrorRespond? {
        if (TaskRepository.deleteTask(taskId)) {
            return null
        } else {
            return ErrorRespond(
                HttpStatusCode.NotFound.value,
                "The tasks id could not be found",
            )
        }
    }

    // Method that will update a task information and if the id is not found
    // it will return ErrorResponse
    fun updateTask(task: Task): ErrorRespond? {
        if (TaskRepository.updateTask(task)) {
            return null
        } else {
            return ErrorRespond(
                HttpStatusCode.NotFound.value,
                "The tasks id could not be found",
            )
        }
    }
}