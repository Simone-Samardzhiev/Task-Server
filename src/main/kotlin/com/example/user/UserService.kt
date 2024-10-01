package com.example.user

import com.auth0.jwt.JWT
import com.example.jwt.JWTUserService

// Service used to manage the data of the users
object UserService: UserServiceInterface {
    // Method that will check for valid email syntax
    override suspend fun validateEmail(email: String): Boolean {
        val regex = "^[\\w._%+0-]+@[\\w._-]+\\.[a-zA-Z]{2,}$".toRegex()
        return email.matches(regex)
    }

    // Method that will check if a password is secure enough
    override suspend fun validatePassword(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$".toRegex()
        return password.matches(regex)
    }

    // Method that will add a new user
    override suspend fun addUser(user: User) {
        if (UserRepository.checkEmail(user.email)) {
            throw EmailInUserError()
        }

        if (!validateEmail(user.email)) {
            throw InvalidEmailError()
        }

        if (!validatePassword(user.password)) {
            throw InvalidPasswordError()
        }
    }

    // Method that will crete a token for a user
    override suspend fun getToken(user: User): String {
        val userId = UserRepository.checkUserCredentials(user)

        userId?.let {
            return JWTUserService.generateToken(it)
        } ?: throw WrongCredentialsError()
    }
}