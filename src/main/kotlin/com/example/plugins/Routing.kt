package com.example.plugins

import com.example.models.ErrorRespond
import com.example.models.User
import com.example.services.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/user") {
            get("/login/{email}/{password}") {
                val email = call.parameters["email"]
                val password = call.parameters["password"]

                if (email != null && password != null) {
                    val token = UserService.loginUser(
                        User(
                            email,
                            password
                        )
                    )

                    if (token != null) {
                        call.respond(HttpStatusCode.OK, token)
                    } else {
                        call.respond(
                            ErrorRespond(
                                HttpStatusCode.Unauthorized.value,
                                "The email the password is wrong."
                            )
                        )
                    }
                } else {
                    call.respond(
                        ErrorRespond(
                            HttpStatusCode.BadRequest.value,
                            "The email or password is parameter is missing."
                        )
                    )
                }
            }
        }
    }
}

