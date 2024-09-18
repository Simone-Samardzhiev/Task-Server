package com.example.services

// Service that will manage the users
object UserService {
    // Method that will check if the email is valid.
    private fun isValidEmail(email: String): Boolean {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.isBlank() && email.matches(regex)
    }

}