package com.example.task.service

import com.example.user.repository.UserRepositoryInterface


class TaskService(override val userRepository: UserRepositoryInterface): TaskServiceInterface {
}