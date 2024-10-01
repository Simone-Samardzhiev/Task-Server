package com.example.user

// Error throws when an email is already in use
class EmailInUserError() : RuntimeException()
// Error throws when an email is invalid
class InvalidEmailError() : RuntimeException()
// Error thrown when the password in invalid
class InvalidPasswordError() : RuntimeException()
// Error throw when a user try to register and the credentials are wrong
class WrongCredentialsError() : RuntimeException()