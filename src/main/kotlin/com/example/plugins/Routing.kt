package com.example.plugins

import com.example.task.service.TaskServiceInterface
import com.example.user.error.EmailInUserError
import com.example.user.error.InvalidEmailError
import com.example.user.error.InvalidPasswordError
import com.example.user.error.WrongCredentialsError
import com.example.user.model.User
import com.example.user.service.UserServiceInterface
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

/**
 * Extension function that will set the routing of the server.
 */
fun Application.configureRouting(userService: UserServiceInterface, taskService: TaskServiceInterface) {
    routing {
        // Route for users
        route("/users") {
            // Method used to register a user.
            post("/register") {
                try {
                    // Getting the user from the body
                    val user = call.receive<User>()
                    // Registering the user
                    userService.addUser(user)
                    // Response if the registration was successfully
                    call.respond(
                        HttpStatusCode.OK,
                        "User successfully registered."
                    )
                } catch (_: ContentTransformationException) {
                    // Response if the user information couldn't be got from the body
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "The information from the json body could not be found."
                    )
                } catch (_: EmailInUserError) {
                    // Response if the email is already in use.
                    call.respond(
                        HttpStatusCode.Conflict,
                        "The email is already in use."
                    )
                } catch (_: InvalidEmailError) {
                    // Response if the email syntax is invalid
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "The email is invalid."
                    )
                } catch (_: InvalidPasswordError) {
                    // Response if the password is not strong enough
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "The password is invalid."
                    )
                }
            }

            // Method used to log in that will return a JWT
            post("/login") {
                try {
                    // Getting the user information
                    val user = call.receive<User>()
                    // Creating the token for the user
                    val token = userService.getToken(user)
                    // Response if the token was created successfully
                    call.respond(HttpStatusCode.OK, token)
                } catch (_: ContentTransformationException) {
                    // Response if the user information couldn't be got from the body
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "The information from the json body could not be found."
                    )
                } catch (_: WrongCredentialsError) {
                    // Response if the user credentials are wrong
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        "Wrong credentials."
                    )
                }
            }

            authenticate {
                // Method used to refresh a valid token.
                get("/refreshToken") {
                    // Getting the principle
                    val principal = call.principal<JWTPrincipal>()

                    principal?.let {
                        // Response with the new token
                        call.respond(
                            HttpStatusCode.OK,
                            userService.jwtUserService.refreshToken(it)
                        )
                    } ?: call.respond( // Response if the token couldn't be found
                        HttpStatusCode.Unauthorized,
                        "The JWT token could not be found."
                    )
                }
            }
        }
        authenticate {
            route("/tasks") {
                get {
                    val principal = call.principal<JWTPrincipal>()

                    principal?.let {
                        call.respond(
                            HttpStatusCode.OK,
                            taskService.getTasks(it)
                        )
                    }?: call.respond(
                        HttpStatusCode.Unauthorized,
                        "The JWT token could not be found."
                    )
                }
            }
        }
    }
}