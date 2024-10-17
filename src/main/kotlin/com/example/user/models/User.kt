package com.example.user.models

import kotlinx.serialization.Serializable

/**
 * Class used to create user.
 * @param email The email of the user.
 * @param password The password of the user.
 */
@Serializable
data class User(val email: String, val password: String)
