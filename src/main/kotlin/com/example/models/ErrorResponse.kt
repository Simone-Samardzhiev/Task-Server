package com.example.models

import kotlinx.serialization.Serializable

// Class used to send a message and error code
@Serializable
data class ErrorResponse(
    val code: Int,
    val message: String
)
