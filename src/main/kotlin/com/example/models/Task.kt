package com.example.models

import kotlinx.serialization.Serializable

enum class Priority {
    low, medium, high, vital
}

@Serializable
data class Task(val name: String, val description: String, val priority: Priority)