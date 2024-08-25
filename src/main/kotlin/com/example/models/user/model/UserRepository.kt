package com.example.models.user.model

import com.example.tables.UserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

// Repository used to get user data from the table.
object UserRepository {
    // Method that will check if a user with this email exist.
    suspend fun checkIfEmailInUse(email: String): Boolean {
        return transaction {
            UserTable
                .select(UserTable.email)
                .where { UserTable.email eq email }
                .count() > 0
        }
    }

    // Method that will add register a user.
    suspend fun registerUser(user: User): Boolean {
        if (checkIfEmailInUse(user.email)) {
            return false
        }

        transaction {
            UserTable
                .insert {
                    it[id] = UUID.randomUUID()
                    it[email] = user.email
                    it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt())
                }
        }

        return true
    }

    // Method that will check the user credentials.
    suspend fun checkUserCredentials(user: User): Boolean {
        return transaction {
            val foundUser = UserTable
                .select(UserTable.password)
                .where(UserTable.email eq user.email)
                .singleOrNull()

            foundUser?.let {
                BCrypt.checkpw(user.password, it[UserTable.password])
            } ?:  false
        }
    }
}