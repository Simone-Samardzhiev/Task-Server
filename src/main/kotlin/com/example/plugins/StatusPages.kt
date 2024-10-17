package com.example.plugins

import com.example.task.errors.TaskIdNotFoundException
import com.example.user.errors.EmailInUseException
import com.example.user.errors.InvalidEmailException
import com.example.user.errors.InvalidPasswordException
import com.example.user.errors.WrongCredentialsException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.respond
import kotlinx.serialization.SerializationException

fun Application.configureStatusPages() {
    install(StatusPages) {

        /**
         * Handles exceptions by responding to the client with the specified status code and message.
         *
         * @param call The [ApplicationCall] instance representing the client's request and the server's response context.
         * This allows the function to respond to the client with appropriate HTTP status codes and messages.
         * @param cause The exception that was thrown.
         * @param statusCode The status code that should be sent back to the client, indicating the result of the request processing.
         */
        suspend fun <T : RuntimeException> handleException(call: ApplicationCall, cause: T, statusCode: HttpStatusCode) {
            val message = cause.message ?: "There was an error."
            call.respond(statusCode, message)
        }

        exception<SerializationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, "The JSON data is not valid.")
        }

        exception<TaskIdNotFoundException> { call, cause ->
            handleException(call, cause, HttpStatusCode.NotFound)
        }

        exception<EmailInUseException> { call, cause ->
            handleException(call, cause, HttpStatusCode.Conflict)
        }

        exception<InvalidEmailException> { call, cause ->
            handleException(call, cause, HttpStatusCode.BadRequest)
        }

        exception<InvalidPasswordException> { call, cause ->
            handleException(call, cause, HttpStatusCode.BadRequest)
        }

        exception<WrongCredentialsException> { call, cause ->
            handleException(call, cause, HttpStatusCode.Unauthorized)
        }
    }
}