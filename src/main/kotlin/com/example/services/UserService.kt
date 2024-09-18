package com.example.services
import com.example.models.User
import com.example.repositories.UserRepository

// Service that will manage the users
object UserService {
    // Method that will check if the email is valid.
    private fun isValidEmail(email: String): Boolean {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.isBlank() && email.matches(regex) && !UserRepository.checkIfEmailExists(email)
    }

    // Method that will check if the password is valid.
    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=])(?=\\\\S+\\$).{8,}$".toRegex()
        return password.matches(regex)
    }

    // Method that will register a user
    fun registerUser(user: User): Boolean {
        if (!isValidEmail(user.email) || !isValidPassword(user.password)) {
            return false
        }

        UserRepository.addUser(user)
        return true
    }
}