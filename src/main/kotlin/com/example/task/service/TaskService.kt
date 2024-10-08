package com.example.task.service

import com.example.task.model.Task
import com.example.task.repository.TaskRepositoryInterface
import java.util.UUID


class TaskService(override val taskRepository: TaskRepositoryInterface) : TaskServiceInterface {
    override suspend fun getTasks(userId: UUID): List<Task> = taskRepository.getTasksByUserId(userId)

}