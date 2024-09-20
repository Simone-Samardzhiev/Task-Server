package com.example.services
import com.example.models.ErrorRespond
import com.example.models.User
import com.example.repositories.UserRepository
import io.ktor.http.HttpStatusCode

// Service that will manage the users
object UserService {
    // Method that will check if the email is valid.
    private fun isValidEmail(email: String): Boolean {
        return !email.isBlank() || !email.contains("@")
    }

    // Method that will check if the password is valid.
    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_@#$%^&+=])(?=\\S+\$).{8,}$".toRegex()
        return password.matches(regex)
    }

    // Method that will register a user
    fun registerUser(user: User): ErrorRespond? {
        if (!UserRepository.checkIfEmailExists(user.email)) {
            return ErrorRespond(
                HttpStatusCode.BadRequest.value,
                "The email is already in use"
            )
        }

        if (!isValidEmail(user.email)) {
            return ErrorRespond(
                HttpStatusCode.BadRequest.value,
                "The email cannot be blank and it must contain @"
            )
        }

        if (!isValidPassword(user.password)) {
            return ErrorRespond(
                HttpStatusCode.BadRequest.value,
                "The password doesn't pass the security requirements"
            )
        }

        UserRepository.addUser(user)
        return null
    }

    // Method that will return a JWT token if the user credentials are valid
    fun loginUser(user: User): String? {
        val id = UserRepository.checkUserCredentials(user)

        id?.let {
            return JWTService.createToken(it)
        } ?: return null
    }
}