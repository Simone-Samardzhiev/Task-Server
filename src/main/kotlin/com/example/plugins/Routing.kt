package com.example.plugins

import com.example.models.ErrorRespond
import com.example.models.Task
import com.example.models.TaskWithoutId
import com.example.models.User
import com.example.repositories.UserRepository
import com.example.services.TaskService
import com.example.services.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.util.UUID

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
            post("/register") {
                try {
                    val user = call.receive<User>()
                    if (UserService.registerUser(user)) {
                        call.respond(HttpStatusCode.Created)
                    } else {
                        call.respond(
                            ErrorRespond(
                                HttpStatusCode.BadRequest.value,
                                "The email or password is unavailable."
                            )
                        )
                    }
                } catch (e: ContentTransformationException) {
                    call.respond(
                        ErrorRespond(
                            HttpStatusCode.BadRequest.value,
                            "The user information could not be found in the body."
                        )
                    )
                }
            }
        }
        authenticate {
            route("/tasks") {
                get {
                    val principal = call.principal<JWTPrincipal>()
                    if (principal != null) {
                        val userId = UUID.fromString(principal.payload.getClaim("id").asString())
                        call.respond(HttpStatusCode.OK, TaskService.getTasks(userId))
                    }
                }
                post {
                    try {
                        val task = call.receive<TaskWithoutId>()
                    } catch (e: ContentTransformationException) {
                        call.respond(
                            ErrorRespond(
                                HttpStatusCode.BadRequest.value,
                                "The details about the task could not be found in the body."
                            )
                        )
                    }
                }

                put {
                    try {
                        val task = call.receive<Task>()
                        val errorRespond = TaskService.updateTask(task)

                        if (errorRespond != null) {
                            call.respond(errorRespond)
                        } else {
                            call.respond(HttpStatusCode.OK)
                        }

                    } catch (e: ContentTransformationException) {
                        call.respond(
                            ErrorRespond(
                                HttpStatusCode.BadRequest.value,
                                "The details about the task could not be found in the body."
                            )
                        )
                    }
                }
                
                delete("/{id}") {
                    try {
                        val stringId = call.parameters["id"]
                        if (stringId != null) {
                            val id = UUID.fromString(stringId)
                            val errorRespond = TaskService.deleteTask(id)

                            if (errorRespond != null) {
                               call.respond(errorRespond)
                            } else {
                                call.respond(HttpStatusCode.OK)
                            }
                        }
                    } catch (e: IllegalArgumentException) {
                        call.respond(
                            ErrorRespond(
                                HttpStatusCode.BadRequest.value,
                                "The id of the task could not be found."
                            )
                        )
                    }
                }
            }
        }
    }
}

