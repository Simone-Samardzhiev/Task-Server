package com.example.user.repository

import com.example.user.model.User
import java.util.UUID

// Interface used to create user repository
interface UserRepositoryInterface {
    // Method used to check if an email exist
    suspend fun checkEmail(email: String): Boolean

    // Method used to add a new user
    suspend fun createUser(user: User)

    // Method used to check user credentials or return the user id if they are correct
    suspend fun checkUserCredentials(user: User): UUID?

    // Method that will check if the user id is valid
    suspend fun checkUserId(userId: UUID): Boolean
}