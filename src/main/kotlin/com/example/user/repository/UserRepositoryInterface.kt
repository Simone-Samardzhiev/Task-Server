package com.example.user.repository

import com.example.user.models.User
import java.util.UUID

/**
 * Interface used to create user repository.
 */
interface UserRepositoryInterface {
    /**
     * Method that will check if an email is in use.
     * @param email The email of the user.
     * @return True if the email is in user otherwise false.
     */
    suspend fun checkEmail(email: String): Boolean

    /**
     * Method that will create a new user.
     * @param user The user information.
     */
    suspend fun createUser(user: User)

    /**
     *  Method that will check user credentials.
     *  @param user The user credentials.
     *  @return The user id if the user credentials are correct otherwise false.
     */
    suspend fun checkUserCredentials(user: User): UUID?

    /**
     * Method that will check if an id exist
     * @param userId The user id that will be checked if it exists.
     * @return True if the id exists otherwise false.
     */
    suspend fun checkUserId(userId: UUID): Boolean
}