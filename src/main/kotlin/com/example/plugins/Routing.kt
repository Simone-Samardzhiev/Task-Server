package com.example.plugins

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

fun Application.configureRouting(userService: UserServiceInterface) {
    routing {
        route("/users") {
            post("/register") {
                try {
                    val user = call.receive<User>()
                    userService.addUser(user)
                    call.respond(
                        HttpStatusCode.OK,
                        "User successfully registered."
                    )
                } catch (_: ContentTransformationException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "The information from the json body could not be found."
                    )
                } catch (_: EmailInUserError) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        "The email is already in use."
                    )
                } catch (_: InvalidEmailError) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "The email is invalid."
                    )
                } catch (_: InvalidPasswordError) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "The password is invalid."
                    )
                }
            }

            post("/login") {
                try {
                    val user = call.receive<User>()
                    val token = userService.getToken(user)
                    call.respond(HttpStatusCode.OK, token)
                } catch (_: ContentTransformationException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "The information from the json body could not be found."
                    )
                } catch (_: WrongCredentialsError) {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        "Wrong credentials."
                    )
                }
            }

            authenticate {
                get("/refreshToken") {
                    val principal = call.principal<JWTPrincipal>()

                    principal?.let {
                        call.respond(
                            HttpStatusCode.OK,
                            userService.refreshToken(principal)
                        )
                    } ?: call.respond(
                        HttpStatusCode.Unauthorized,
                        "The JWT token could not be found."
                    )
                }
            }
        }
    }
}