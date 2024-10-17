package com.example.plugins

import com.example.task.models.NewTask
import com.example.task.models.Task
import com.example.task.services.TaskServiceInterface
import com.example.user.models.User
import com.example.user.services.UserServiceInterface
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
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
                // Getting the user from the body
                val user = call.receive<User>()
                // Registering the user
                userService.addUser(user)
                // Response if the registration was successfully
                call.respond(
                    HttpStatusCode.OK,
                    "User successfully registered."
                )
            }

            // Method used to log in that will return a JWT
            post("/login") {
                // Getting the user information
                val user = call.receive<User>()
                // Creating the token for the user
                val token = userService.getToken(user)
                // Response if the token was created successfully
                call.respond(HttpStatusCode.OK, token)
            }

            authenticate {
                // Method used to refresh a valid token.
                get("/refreshToken") {
                    // Getting the principal
                    val principal = call.principal<JWTPrincipal>()

                    principal?.let {
                        // Response with the new token
                        call.respond(
                            HttpStatusCode.OK,
                            userService.jwtUserService.refreshToken(it)
                        )
                    }
                }
            }
        }
        authenticate {
            // Route for tasks
            route("/tasks") {
                // Method used to get all tasks of a user
                get {
                    val principal = call.principal<JWTPrincipal>()

                    principal?.let {
                        // Response with the tasks
                        call.respond(
                            HttpStatusCode.OK,
                            taskService.getTasks(it)
                        )
                    }
                }
                post {
                    // Getting the principal
                    val principal = call.principal<JWTPrincipal>()

                    principal?.let {
                        // Getting the task
                        val task = call.receive<NewTask>()
                        taskService.addTask(task, it)
                        // Response if the task was added.
                        call.respond(HttpStatusCode.OK)
                    }
                }

                put {
                    // Getting the principal
                    val principal = call.principal<JWTPrincipal>()

                    principal?.let {
                        val task = call.receive<Task>()
                        taskService.updateTask(task)
                        // Response if the task was updated
                        call.respond(HttpStatusCode.OK)
                    }
                }
                delete {
                    // Getting the principal
                    val principal = call.principal<JWTPrincipal>()
                    principal?.let {
                        val task = call.receive<Task>()
                        taskService.deleteTask(task)
                        // Response if the task was deleted
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }
    }
}