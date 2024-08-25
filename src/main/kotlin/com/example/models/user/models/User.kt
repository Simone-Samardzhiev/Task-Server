package com.example.models.user.models

import kotlinx.serialization.Serializable

@Serializable
data class User(val email: String, val password: String)