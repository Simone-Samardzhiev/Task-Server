package com.example.user.jwt

import com.example.user.repository.UserRepositoryInterface
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID

/**
 * Interface used to create JWT service.
 * @property userRepository The user repository.
 * @property secret The secret used to hash the tokens.
 * @property audience The audience that will use the token.
 * @property issuer The issuer who created the token.
 * @property realm The that stores the scope of authentication.
 */
interface JWTUserServiceInterface {
    val userRepository: UserRepositoryInterface
    val secret: String
    val audience: String
    val issuer: String
    val realm: String

    /**
     *  Function used to create a token for a user.
     *  @param userId The id if the user.
     *  @return The generated JWT as a string
     */
    suspend fun generateToken(userId: UUID): String
    /**
     * Function used to validate the credentials of a token.
     * @param credentials The credential of the token.
     * @return The principle if the credentials are correct otherwise null.
     */
    suspend fun validateCredentials(credentials: JWTCredential): JWTPrincipal?

    /**
     * Method used to refresh a token.
     * @param principal The JWT principle.
     */
    suspend fun refreshToken(principal: JWTPrincipal): String
}