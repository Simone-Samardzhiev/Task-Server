package com.example.user

// Interface used to create user service
interface UserServiceInterface {
    // Method that will check for valid email syntax
    suspend fun validateEmail(email: String): Boolean
}