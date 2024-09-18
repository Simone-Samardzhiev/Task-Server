package com.example.models

import kotlinx.serialization.Serializable

// Class used to create users
@Serializable
data class User(
    val email: String,
    val password: String
)
