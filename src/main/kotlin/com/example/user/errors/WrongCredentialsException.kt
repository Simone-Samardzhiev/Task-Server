package com.example.user.errors

/**
 *  Error thrown when the user credentials are wrong.
 */
class WrongCredentialsException() : RuntimeException("The email or the password is wrong.")