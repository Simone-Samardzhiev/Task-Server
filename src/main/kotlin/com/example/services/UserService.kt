package com.example.services

// Service that will manage the users
object UserService {
    // Method that will check if the email is valid.
    private fun isValidEmail(email: String): Boolean {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.isBlank() && email.matches(regex)
    }

    // Method that will check if the password if valid.
    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=])(?=\\\\S+\\$).{8,}$".toRegex()
        return password.matches(regex)
    }
}