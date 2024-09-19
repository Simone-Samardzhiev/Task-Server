package com.example.models

import kotlinx.serialization.Serializable
import java.util.UUID

// Enum class used to create task priority
enum class Priority {
    Low, Medium, High, Vital
}

// Class used to create tasks
@Serializable
data class Task(
    val id: UUID,
    val name: String,
    val description: String,
    val priority: Priority,
)

// Class used to create a task without id
@Serializable
data class TaskWithoutId(
    val name: String,
    val description: String,
    val priority: Priority,
)