package com.example.user.errors

/**
 * Exception thrown when the email of the user is already in use.
 */
class EmailInUseException : RuntimeException("The email is already in user.")