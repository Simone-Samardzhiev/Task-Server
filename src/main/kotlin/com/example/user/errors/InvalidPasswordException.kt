package com.example.user.errors

/**
 * Exception thrown when the password is not secure enough.
 */
class InvalidPasswordException : RuntimeException("Password is invalid.")