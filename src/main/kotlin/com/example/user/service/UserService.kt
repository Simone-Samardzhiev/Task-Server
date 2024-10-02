package com.example.user.service

import com.example.user.EmailInUserError
import com.example.user.InvalidEmailError
import com.example.user.InvalidPasswordError
import com.example.user.User
import com.example.user.WrongCredentialsError
import com.example.user.jwt.JWTUserServiceInterface
import com.example.user.repository.UserRepositoryInterface

// Service used to manage the data of the users
class UserService(
    override val userRepository: UserRepositoryInterface,
    override val jwtUserService: JWTUserServiceInterface
) : UserServiceInterface {
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
        if (userRepository.checkEmail(user.email)) {
            throw EmailInUserError()
        }

        if (!validateEmail(user.email)) {
            throw InvalidEmailError()
        }

        if (!validatePassword(user.password)) {
            throw InvalidPasswordError()
        }

        userRepository.createUser(user)
    }

    // Method that will crete a token for a user
    override suspend fun getToken(user: User): String {
        val userId = userRepository.checkUserCredentials(user)

        userId?.let {
            return jwtUserService.generateToken(it)
        } ?: throw WrongCredentialsError()
    }
}