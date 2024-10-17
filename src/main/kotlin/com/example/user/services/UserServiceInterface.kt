package com.example.user.services

import com.example.user.jwt.JWTUserServiceInterface
import com.example.user.models.User
import com.example.user.repository.UserRepositoryInterface

/**
 * Interface used to create user service.
 * @property userRepository The user repository.
 * @property jwtUserService The JWT service.
 */
interface UserServiceInterface {
    val userRepository: UserRepositoryInterface
    val jwtUserService: JWTUserServiceInterface

    /**
     * Method used to check if an email syntax is valid.
     * @param email The email that will be checked.
     * @return True if the email is valid otherwise false.
     */
    suspend fun validateEmail(email: String): Boolean

    /**
     * Method used to check if a password is strong enough.
     * @param password The password that will be checked.
     * @return True of the password is strong enough otherwise false.
     */
    suspend fun validatePassword(password: String): Boolean

    /**
     * Method that will add a user.
     * @param user The user that will be added.
     */
    suspend fun addUser(user: User)

    /**
     * Method that will create a token.
     * @param user The user to witch the token will be assigned.
     */
    suspend fun getToken(user: User): String
}