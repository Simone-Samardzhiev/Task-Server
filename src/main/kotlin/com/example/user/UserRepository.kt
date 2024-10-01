package com.example.user

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

// User repository that will manage the data of the users
open class UserRepository : UserRepositoryInterface {
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

    // Method used to check user credentials or return the user id if they are correct
    override suspend fun checkUserCredentials(user: User): UUID? {
        val foundUser = transaction {
            UserTable
                .select(UserTable.id, UserTable.password)
                .where { UserTable.email eq user.email }
                .singleOrNull()
        }

        foundUser?.let {
            return if (BCrypt.checkpw(user.password, it[UserTable.password])) {
                it[UserTable.id];
            } else {
                null
            }
        } ?: return null
    }

    // Method that will check if the user id is valid
    override suspend fun checkUserId(userId: UUID): Boolean {
        return transaction {
            UserTable
                .select(UserTable.id)
                .where { UserTable.id eq userId }
                .count() > 0
        }
    }
}