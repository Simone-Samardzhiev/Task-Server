package com.example.user

import java.util.UUID

// Interface used to create user repository
interface UserRepositoryInterface {
    // Method used to check if an email exist
    suspend fun checkEmail(email: String): Boolean
    // Method used to add a new user
    suspend fun createUser(user: User)
    // Method used to check user credentials
    suspend fun checkUser(user: User): Boolean
}