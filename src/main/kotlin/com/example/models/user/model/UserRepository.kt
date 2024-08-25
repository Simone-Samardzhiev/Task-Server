package com.example.models.user.model

import com.example.tables.UserTable
import com.example.tables.UserTable.password
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

// Repository used to get user data from the table.
object UserRepository {
    // Method that will check if a user with this email exist.
    private fun checkIfNotEmailInUse(email: String): Boolean {
        return transaction {
            UserTable
                .select(UserTable.email)
                .where { UserTable.email eq email }
                .empty();
        }
    }

    // Method that will add register a user.
     fun registerUser(user: User): Boolean {
        return transaction {
            if (checkIfNotEmailInUse(user.email)) {
                UserTable
                    .insert {
                        it[id] = UUID.randomUUID()
                        it[email] = user.email
                        it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt())
                    }
                true
            } else {
                false
            }
        }
    }

    // Method that will check the user credentials.
    private fun checkUserCredentials(user: User): Boolean {
        return transaction {
            val foundUser = UserTable
                .select(UserTable.email)
                .where(UserTable.email eq user.email)
                .singleOrNull()

            foundUser?.let {
                BCrypt.checkpw(user.password, it[password])
            } ?: false
        }
    }

    // Method that will return the user id or null if the credentials are incorrect.
    fun getUserId(user: User): UUID? {
        return transaction {
            if (checkUserCredentials(user)) {
                UserTable
                    .select(UserTable.id)
                    .where(UserTable.email eq user.email)
                    .lastOrNull()
                    ?.get(UserTable.id)
            } else {
                null
            }
        }
    }
}