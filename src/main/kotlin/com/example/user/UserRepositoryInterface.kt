package com.example.user

// Interface used to create user repository
interface UserRepositoryInterface {
    // Method used to check if an email exist
    suspend fun checkEmail(email: String): Boolean
}