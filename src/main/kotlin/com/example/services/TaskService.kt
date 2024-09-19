package com.example.services

import com.example.models.Task
import com.example.models.TaskWithoutId
import com.example.repositories.TaskRepository
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
}