package com.example.models.response.models

import com.example.models.task.models.TaskWithoutId
import com.example.models.user.models.User
import kotlinx.serialization.Serializable


@Serializable
data class AddTaskRequestBody(
    val user: User,
    val task: TaskWithoutId
)