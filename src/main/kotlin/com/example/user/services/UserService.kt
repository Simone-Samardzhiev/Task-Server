package com.example.user.services

import com.example.user.errors.EmailInUseException
import com.example.user.errors.InvalidEmailException
import com.example.user.errors.InvalidPasswordException
import com.example.user.errors.WrongCredentialsException
import com.example.user.jwt.JWTUserServiceInterface
import com.example.user.models.User
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
     * @throws EmailInUseException when the email is already in use.
     * @throws InvalidEmailException when the provided email is invalid.
     * @throws InvalidPasswordException when the provided password is weak.
     */
    override suspend fun addUser(user: User) {
        if (userRepository.checkEmail(user.email)) {
            throw EmailInUseException()
        }

        if (!validateEmail(user.email)) {
            throw InvalidEmailException()
        }

        if (!validatePassword(user.password)) {
            throw InvalidPasswordException()
        }

        userRepository.createUser(user)
    }

    /**
     * Method that will create a token.
     * @param user The user to which the token will be assigned.
     * @return The generated token as a String.
     * @throws WrongCredentialsException When the user credentials are invalid.
     */
    override suspend fun getToken(user: User): String {
        val userId = userRepository.checkUserCredentials(user)

        userId?.let {
            return jwtUserService.generateToken(it)
        } ?: throw WrongCredentialsException()
    }
}
