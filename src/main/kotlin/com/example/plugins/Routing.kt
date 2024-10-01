package com.example.plugins

import com.example.user.EmailInUserError
import com.example.user.InvalidEmailError
import com.example.user.InvalidPasswordError
import com.example.user.User
import com.example.user.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        route("/users") {
            post("/register") {
                try {
                    val user = call.receive<User>()
                    UserService.addUser(user)
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
        }
    }
}