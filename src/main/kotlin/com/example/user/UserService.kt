package com.example.user

// Service used to manage the data of the users
object UserService: UserServiceInterface {
    // Method that will check for valid email syntax
    override suspend fun validateEmail(email: String): Boolean {
        val regex = "^[\\w._%+0-]+@[\\w._-]+\\.[a-zA-Z]{2,}$".toRegex()
        return email.matches(regex)
    }
}