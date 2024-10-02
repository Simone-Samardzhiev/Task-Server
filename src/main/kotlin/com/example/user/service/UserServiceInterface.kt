package com.example.user.service

import com.example.user.User
import com.example.user.jwt.JWTUserServiceInterface
import com.example.user.repository.UserRepositoryInterface

// Interface used to create user service
interface UserServiceInterface {
    // The user repository
    val userRepository: UserRepositoryInterface

    // The JWT user service
    val jwtUserService: JWTUserServiceInterface

    // Method that will check for valid email syntax
    suspend fun validateEmail(email: String): Boolean

    // Method that will check if a password is secure enough
    suspend fun validatePassword(password: String): Boolean

    // Method that will add a new user
    suspend fun addUser(user: User)

    // Method that will crete a token for a user
    suspend fun getToken(user: User): String
}