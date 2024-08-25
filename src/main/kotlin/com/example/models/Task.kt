package com.example.models

enum class Priority {
    low, medium, high, vital
}

data class Task(val name: String, val description: String, val priority: Priority)