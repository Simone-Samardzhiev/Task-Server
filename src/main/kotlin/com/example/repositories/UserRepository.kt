package com.example.repositories

import com.example.models.User
import com.example.tables.UserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

// Object that will manage the data in of the users
object UserRepository {
    // Method that will check if an email is already in use.
    fun checkIfEmailExists(email: String): Boolean {
        return transaction {
            UserTable
                .select(UserTable.email)
                .where {UserTable.email eq email}
                .empty()
        }
    }
}