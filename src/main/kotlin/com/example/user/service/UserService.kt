package com.example.user.service

import com.example.user.error.EmailInUserError
import com.example.user.error.InvalidEmailError
import com.example.user.error.InvalidPasswordError
import com.example.user.model.User
import com.example.user.error.WrongCredentialsError
import com.example.user.jwt.JWTUserServiceInterface
import com.example.user.repository.UserRepositoryInterface
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID

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
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_@$!%*?&#])[A-Za-z\\d_@$!%*?&#]{8,}$".toRegex()
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

    // Method that will create a new token using a previous one
    override suspend fun refreshToken(principal: JWTPrincipal): String {
        val id = UUID.fromString(principal.payload.getClaim("id").asString())
        return jwtUserService.generateToken(id)
    }
}