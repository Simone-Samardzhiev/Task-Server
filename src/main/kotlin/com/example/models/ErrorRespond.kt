package com.example.models

import kotlinx.serialization.Serializable

// Class used to send a message and error code
@Serializable
data class ErrorRespond(
    val code: Int,
    val message: String
)
