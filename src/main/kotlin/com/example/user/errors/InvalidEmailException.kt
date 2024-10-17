package com.example.user.errors

/**
 * Exception thrown when the syntax of the email is invalid.
 */
class InvalidEmailException : RuntimeException("The email is invalid.")