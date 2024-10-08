package com.example.task.service

import com.example.user.repository.UserRepositoryInterface

/**
 * Interface used to create task service.
 * @property userRepository The task repository.
 */
interface TaskServiceInterface {
    val userRepository: UserRepositoryInterface
}