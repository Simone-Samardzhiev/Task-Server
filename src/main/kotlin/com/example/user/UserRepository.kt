package com.example.user

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

// User repository that will manage the data of the users
object UserRepository: UserRepositoryInterface {
    // Method used to check if an email exist
    override suspend fun checkEmail(email: String): Boolean {
        return transaction {
            UserTable
                .select(UserTable.email)
                .where { UserTable.email eq email }
                .count() > 0
        }
    }
}