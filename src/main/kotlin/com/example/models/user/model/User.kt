package com.example.models.user.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, val password: String)