package com.example.user

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

// User repository that will manage the data of the users
object UserRepository : UserRepositoryInterface {
    // Method used to check if an email exist
    override suspend fun checkEmail(email: String): Boolean {
        return transaction {
            UserTable
                .select(UserTable.email)
                .where { UserTable.email eq email }
                .count() > 0
        }
    }

    // Method used to add a new user
    override suspend fun createUser(user: User) {
        transaction {
            UserTable.insert {
                it[id] = UUID.randomUUID()
                it[email] = user.email
                it[password] = user.password
            }
        }
    }
}