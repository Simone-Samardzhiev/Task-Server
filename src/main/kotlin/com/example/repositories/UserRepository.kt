package com.example.repositories

import com.example.models.User
import com.example.tables.UserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.*

// Object that will manage the data in of the users
object UserRepository {
    // Method that will check if an email is already in use.
    fun checkIfEmailExists(email: String): Boolean {
        return transaction {
            UserTable
                .select(UserTable.email)
                .where { UserTable.email eq email }
                .empty()
        }
    }

    // Method that will add a new user
    fun addUser(user: User) {
        val encryptedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())
        transaction {
            UserTable
                .insert {
                    it[UserTable.id] = UUID.randomUUID()
                    it[UserTable.email] = user.email
                    it[UserTable.password] = encryptedPassword
                }
        }
    }
}