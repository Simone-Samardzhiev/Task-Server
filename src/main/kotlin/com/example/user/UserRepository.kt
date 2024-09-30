package com.example.user

import org.jetbrains.exposed.sql.Select
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
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
                it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt())
            }
        }
    }

    // Method used to check user credentials
    override suspend fun checkUser(user: User): Boolean {
        val foundUser = transaction {
            UserTable
                .select(UserTable.email, UserTable.password)
                .where { UserTable.email eq user.email }
                .singleOrNull()
        }

        foundUser?.let {
            return BCrypt.checkpw(user.password, it[UserTable.password])
        } ?: return false
    }
}