package com.example.models.taskModel

import kotlinx.serialization.Serializable

enum class Priority {
    low, medium, high, vital
}

@Serializable
data class Task(val name: String, val description: String, val priority: Priority)