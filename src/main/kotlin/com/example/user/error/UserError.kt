package com.example.user.error

/**
 * Error thrown when the email of the user is already in use.
 */
class EmailInUserError() : RuntimeException()

/**
 * Error thrown when the syntax of the email is invalid
 */
class InvalidEmailError() : RuntimeException()

/**
 * Error thrown when the password is not secure enough.
 */
class InvalidPasswordError() : RuntimeException()

/**
 *  Error thrown when the user credentials are wrong.
 */
class WrongCredentialsError() : RuntimeException()