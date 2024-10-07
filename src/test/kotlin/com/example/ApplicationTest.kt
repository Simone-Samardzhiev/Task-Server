package com.example


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import com.example.user.error.EmailInUserError
import com.example.user.error.InvalidEmailError
import com.example.user.error.InvalidPasswordError
import com.example.user.error.WrongCredentialsError
import com.example.user.jwt.JWTUserServiceInterface
import com.example.user.model.User
import com.example.user.repository.UserRepositoryInterface
import com.example.user.service.UserServiceInterface
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import java.util.Date
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals


data class FakeUser(val id: UUID, val user: User)

class FakeUserRepository : UserRepositoryInterface {
    val list: MutableList<FakeUser> = mutableListOf()

    override suspend fun checkEmail(email: String): Boolean {
        for (user in list) {
            if (user.user.email == email) {
                return true
            }
        }

        return false
    }

    override suspend fun createUser(user: User) {
        list.add(FakeUser(UUID.randomUUID(), user))
    }

    override suspend fun checkUserCredentials(user: User): UUID? {
        for (user in list) {
            if (user.user == user) {
                return user.id
            }
        }

        return null
    }

    override suspend fun checkUserId(userId: UUID): Boolean {
        for (user in list) {
            if (user.user == userId) {
                return true
            }
        }
        return false
    }
}

class FakeJWTUserService(
    override val userRepository: UserRepositoryInterface
) : JWTUserServiceInterface {
    // The secret used to hash the token
    override val secret = System.getenv("secret") ?: throw RuntimeException("Missing secret!")

    // The audience that will use the token
    override val audience = "Task-App-Client"

    // The issuer who created the token
    override val issuer: String = "http://localhost:8080"

    // The realm that stores the scope of authentication
    override val realm: String = "Task-App"

    // Method used to create token
    override suspend fun generateToken(userId: UUID): String {
        return JWT
            .create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("id", userId.toString())
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + 60000 * 5))
            .sign(Algorithm.HMAC256(secret))
    }

    // Method used to check the JWT credentials
    override suspend fun validateCredentials(credentials: JWTCredential): JWTPrincipal? {
        return if (credentials.payload.audience.contains(audience) && credentials.payload.issuer.equals(issuer)) {
            val claim = credentials.payload.getClaim("id").asString()
            val id = UUID.fromString(claim)

            return if (userRepository.checkUserId(id)) {
                JWTPrincipal(credentials.payload)
            } else {
                null
            }
        } else {
            null
        }
    }
}

class FakeUserService(
    override val userRepository: UserRepositoryInterface,
    override val jwtUserService: JWTUserServiceInterface
) : UserServiceInterface {
    override suspend fun validateEmail(email: String): Boolean {
        val regex = "^[\\w._%+0-]+@[\\w._-]+\\.[a-zA-Z]{2,}$".toRegex()
        return email.matches(regex)
    }

    override suspend fun validatePassword(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[_@$!%*?&#])[A-Za-z\\d_@$!%*?&#]{8,}$".toRegex()
        return password.matches(regex)
    }

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

    override suspend fun getToken(user: User): String {
        val userId = userRepository.checkUserCredentials(user)

        userId?.let {
            return jwtUserService.generateToken(it)
        } ?: throw WrongCredentialsError()
    }
}

class ApplicationTest {
    fun ApplicationTestBuilder.createEnvironment() {
        val fakeUserRepository = FakeUserRepository()
        val jwtUserService = FakeJWTUserService(fakeUserRepository)
        val fakeUserService = FakeUserService(fakeUserRepository, jwtUserService)

        application {
            configureSerialization()
            configureRouting(fakeUserService)
        }
    }

    fun ApplicationTestBuilder.createClient(): HttpClient {
        return createClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    @Test
    fun testRegister() = testApplication {
        createEnvironment()
        val client = createClient()


        val validUser = User(
            "simone@gmail.com",
            "Simone_2006",
        )

        client.post("users/register") {
            contentType(ContentType.Application.Json)
            setBody(validUser)
        }.apply {
            print(bodyAsText())
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testDuplicatedEmail() = testApplication {
        createEnvironment()
        val client = createClient()

        val validUser = User(
            "simone@gmail.com",
            "Simone_2006",
        )

        client.post("users/register") {
            contentType(ContentType.Application.Json)
            setBody(validUser)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        client.post("users/register") {
            contentType(ContentType.Application.Json)
            setBody(validUser)
        }.apply {
            assertEquals(HttpStatusCode.Conflict, status)
            assertEquals("The email is already in use.", bodyAsText())
        }
    }

    @Test
    fun testInvalidEmail() = testApplication {
        createEnvironment()
        val client = createClient()

        val invalidEmailUser = User(
            "simone",
            "Simone_2006",
        )

        client.post("users/register") {
            contentType(ContentType.Application.Json)
            setBody(invalidEmailUser)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("The email is invalid.", bodyAsText())
        }
    }

    @Test
    fun testInvalidPassword() = testApplication {
        createEnvironment()
        val client = createClient()

        val invalidPasswordUser = User(
            "simone@gmail.com",
            "Simone2006",
        )

        client.post("users/register") {
            contentType(ContentType.Application.Json)
            setBody(invalidPasswordUser)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("The password is invalid.", bodyAsText())
        }
    }

    @Test
    fun testMissingBody() = testApplication {
        createEnvironment()
        val client = createClient()

        client.post("users/register") {

        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("The information from the json body could not be found.", bodyAsText())
        }
    }
}
