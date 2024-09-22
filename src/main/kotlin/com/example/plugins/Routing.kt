package com.example.plugins

import com.example.exception.EmailInUseException
import com.example.exception.InvalidEmailException
import com.example.exception.InvalidPasswordException
import com.example.models.ErrorRespond
import com.example.models.Task
import com.example.models.TaskWithoutId
import com.example.models.User
import com.example.services.JWTService
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
            post("/login") {
                try {
                    val user = call.receive<User>()
                    val token = UserService.loginUser(user)

                    if (token != null) {
                        call.respond(HttpStatusCode.OK, token)
                    } else {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            ErrorRespond(
                                HttpStatusCode.Unauthorized.value,
                                "The email of the or the password is wrong"
                            )
                        )
                    }
                } catch (_: ContentTransformationException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorRespond(
                            HttpStatusCode.BadRequest.value,
                            "The user information could not be found in the body."
                        )
                    )
                }
            }
            post("/register") {
                try {
                    val user = call.receive<User>()
                    UserService.registerUser(user)
                    call.respond(HttpStatusCode.Created)

                } catch (_: ContentTransformationException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorRespond(
                            HttpStatusCode.BadRequest.value,
                            "The user information could not be found in the body."
                        )
                    )
                } catch (_: EmailInUseException) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        ErrorRespond(
                            HttpStatusCode.Conflict.value,
                            "The email is already in use"
                        )
                    )
                } catch (_: InvalidEmailException) {
                    call.respond(
                        HttpStatusCode.NotAcceptable,
                        ErrorRespond(
                            HttpStatusCode.NotAcceptable.value,
                            "The email is invalid"
                        )
                    )
                } catch (_: InvalidPasswordException) {
                    call.respond(
                        HttpStatusCode.NotAcceptable,
                        ErrorRespond(
                            HttpStatusCode.NotAcceptable.value,
                            "The password is invalid"
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
                        val principal = call.principal<JWTPrincipal>()

                        if (principal != null) {
                            val userId = UUID.fromString(principal.payload.getClaim("id").asString())
                            TaskService.addTask(task, userId)
                            call.respond(HttpStatusCode.OK)
                        }
                    } catch (_: ContentTransformationException) {
                        call.respond(
                            HttpStatusCode.BadRequest,
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
                            call.respond(HttpStatusCode.NotFound, errorRespond)
                        } else {
                            call.respond(HttpStatusCode.OK)
                        }

                    } catch (_: ContentTransformationException) {
                        call.respond(
                            HttpStatusCode.BadRequest,
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
                        } else {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                ErrorRespond(
                                    HttpStatusCode.BadRequest.value, "The id of the task could not be found."
                                )
                            )
                        }
                    } catch (_: IllegalArgumentException) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ErrorRespond(
                                HttpStatusCode.BadRequest.value, "The id of the task could not be found."
                            )
                        )
                    }
                }
            }
            get("refreshToken") {
                val principal = call.principal<JWTPrincipal>()

                if (principal != null) {
                    val userId = UUID.fromString(principal.payload.getClaim("id").asString())
                    call.respond(HttpStatusCode.OK, JWTService.createToken(userId))
                }
            }
        }
    }
}

