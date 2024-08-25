package com.example.models.user.model

import com.example.tables.UserTable
import org.jetbrains.exposed.sql.transactions.transaction

// Repository used to get user data from the table.
object UserRepository {
    // Method that will check if a user with this email exist.
    suspend fun checkIfEmailInUse(email:String): Boolean {
        return transaction {
            UserTable
                .select(UserTable.email)
                .where { UserTable.email eq email }
                .count() > 0
        }
    }
}