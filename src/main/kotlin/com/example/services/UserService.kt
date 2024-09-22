package com.example.services
import com.example.exception.EmailInUseException
import com.example.exception.InvalidEmailException
import com.example.exception.InvalidPasswordException
import com.example.models.ErrorRespond
import com.example.models.User
import com.example.repositories.UserRepository
import io.ktor.http.HttpStatusCode

// Service that will manage the users
object UserService {
    // Method that will check if the email is valid.
    private fun isValidEmail(email: String): Boolean {
        val regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Z|a-z]{2,}\$".toRegex()
        return email.matches(regex)
    }

    // Method that will check if the password is valid.
    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_@#$%^&+=])(?=\\S+\$).{8,}$".toRegex()
        return password.matches(regex)
    }

    // Method that will register a user
    fun registerUser(user: User) {
        if (!UserRepository.checkIfEmailExists(user.email)) {
            throw EmailInUseException()
        }

        if (!isValidEmail(user.email)) {
            throw InvalidEmailException()
        }

        if (!isValidPassword(user.password)) {
            throw InvalidPasswordException()
        }

        UserRepository.addUser(user)
    }

    // Method that will return a JWT token if the user credentials are valid
    fun loginUser(user: User): String? {
        val id = UserRepository.checkUserCredentials(user)

        id?.let {
            return JWTService.createToken(it)
        } ?: return null
    }
}