package com.example.user.service

import com.example.user.error.EmailInUserError
import com.example.user.error.InvalidEmailError
import com.example.user.error.InvalidPasswordError
import com.example.user.error.WrongCredentialsError
import com.example.user.jwt.JWTUserServiceInterface
import com.example.user.model.User
import com.example.user.repository.UserRepositoryInterface

class UserService(
    override val userRepository: UserRepositoryInterface,
    override val jwtUserService: JWTUserServiceInterface
) : UserServiceInterface {

    /**
     * Method used to check if an email syntax is valid.
     * @param email The email that will be checked.
     * @return True if the email is valid otherwise false.
     */
    override suspend fun validateEmail(email: String): Boolean {
        val regex = "^[\\w._%+0-]+@[\\w._-]+\\.[a-zA-Z]{2,}$".toRegex()
        return email.matches(regex)
    }

    /**
     * Method used to check if a password is strong enough.
     * @param password The password that will be checked.
     * @return True if the password is strong enough otherwise false.
     */
    override suspend fun validatePassword(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_@$!%*?&#])[A-Za-z\\d_@$!%*?&#]{8,}$".toRegex()
        return password.matches(regex)
    }

    /**
     * Method that will add a user.
     * @param user The user that will be added.
     * @throws EmailInUserError when the email is already in use.
     * @throws InvalidEmailError when the provided email is invalid.
     * @throws InvalidPasswordError when the provided password is weak.
     */
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

    /**
     * Method that will create a token.
     * @param user The user to which the token will be assigned.
     * @return The generated token as a String.
     * @throws WrongCredentialsError when the user credentials are invalid.
     */
    override suspend fun getToken(user: User): String {
        val userId = userRepository.checkUserCredentials(user)

        userId?.let {
            return jwtUserService.generateToken(it)
        } ?: throw WrongCredentialsError()
    }
}
