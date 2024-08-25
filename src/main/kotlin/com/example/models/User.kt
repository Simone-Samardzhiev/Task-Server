package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, val password: String)