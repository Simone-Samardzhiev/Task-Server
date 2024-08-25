package com.example.plugins


import com.example.models.ErrorResponse
import com.example.models.task.models.TaskRepository
import com.example.models.task.models.TaskWithoutId
import com.example.models.user.models.User
import com.example.models.user.models.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/users") {
            post("/register") {
                val email = call.request.queryParameters["email"]
                val password = call.request.queryParameters["password"]

                if (email == null || password == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(
                            HttpStatusCode.BadRequest.value,
                            "The query parameters email and password must be specified."
                        )
                    )
                } else {
                    if (UserRepository.registerUser(User(email, password))) {
                        call.respond(HttpStatusCode.Created)
                    } else {
                        call.respond(
                            HttpStatusCode.Conflict,
                            ErrorResponse(
                                HttpStatusCode.BadRequest.value,
                                "The email is already in use."
                            )
                        )
                    }
                }
            }
        }
        route("/tasks") {
            get {
                val email = call.request.queryParameters["email"]
                val password = call.request.queryParameters["password"]

                if (email == null || password == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(
                            HttpStatusCode.BadRequest.value,
                            "The query parameters email and password must be specified."
                        )
                    )
                } else {
                    val tasks = TaskRepository.getTasks(User(email, password))

                    tasks?.let {
                        call.respond(HttpStatusCode.OK, it)
                    } ?: call.respond(
                        HttpStatusCode.Unauthorized,
                        ErrorResponse(
                            HttpStatusCode.Unauthorized.value,
                            "The email or the password is invalid."
                        )
                    )
                }
            }

            post("/addTask") {
                val email = call.request.queryParameters["email"]
                val password = call.request.queryParameters["password"]

                if (email == null || password == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(
                            HttpStatusCode.BadRequest.value,
                            "The query parameters email and password must be specified."
                        )
                    )

                } else {
                    try {
                        val task = call.receive<TaskWithoutId>()

                        if (TaskRepository.addTask(User(email, password), task)) {
                            call.respond(HttpStatusCode.NoContent)
                        } else {
                            call.respond(
                                HttpStatusCode.Unauthorized,
                                ErrorResponse(
                                    HttpStatusCode.Unauthorized.value,
                                    "The email or the password is invalid."
                                )
                            )
                        }

                    } catch (e: IllegalArgumentException) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ErrorResponse(
                                HttpStatusCode.BadRequest.value,
                                "The json file could not be read."
                            )
                        )
                    }
                }
            }
        }
    }
}