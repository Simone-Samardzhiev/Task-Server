package com.example.user

// Interface used to create user service
interface UserServiceInterface {
    // Method that will check for valid email syntax
    suspend fun validateEmail(email: String): Boolean
    // Method that will check if a password is secure enough
    suspend fun validatePassword(password: String): Boolean
    // Method that will add a new user
    suspend fun addUser(user: User)
    // Method that will crete a token for a user
    suspend fun getToken(user: User): String
}