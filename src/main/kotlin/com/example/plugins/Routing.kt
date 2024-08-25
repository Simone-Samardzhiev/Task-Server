package com.example.plugins

import com.example.models.errors.model.ErrorResponse
import com.example.models.task.model.TaskRepository
import com.example.models.user.model.User
import com.example.models.user.model.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/users") {
           post("/register") {
               try {
                   val user = call.receive<User>()
                    if (UserRepository.registerUser(user)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(
                            HttpStatusCode.Conflict,
                            ErrorResponse(
                                HttpStatusCode.Conflict.value,
                                "The email is already in use."
                            ))
                    }
               } catch(e: IllegalArgumentException) {
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
        route("/tasks") {
            get {
                try {
                    val user = call.receive<User>()

                    val tasks = TaskRepository.getTasks(user)

                    tasks?.let {
                        call.respond(HttpStatusCode.OK, it)
                    } ?: call.respond(
                        HttpStatusCode.Unauthorized,
                        ErrorResponse(
                            HttpStatusCode.Unauthorized.value,
                            "You email or password is wrong."
                        )
                    )

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