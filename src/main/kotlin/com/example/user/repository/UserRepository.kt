package com.example.user.repository

import com.example.user.model.User
import com.example.user.table.UserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

class UserRepository : UserRepositoryInterface {
    override suspend fun checkEmail(email: String): Boolean {
        return transaction {
            UserTable
                .select(UserTable.email)
                .where { UserTable.email eq email }
                .count() > 0
        }
    }

    override suspend fun createUser(user: User) {
        transaction {
            UserTable.insert {
                it[id] = UUID.randomUUID()
                it[email] = user.email
                it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt())
            }
        }
    }

    override suspend fun checkUserCredentials(user: User): UUID? {
        val foundUser = transaction {
            UserTable
                .select(UserTable.id, UserTable.password)
                .where { UserTable.email eq user.email }
                .singleOrNull()
        }

        foundUser?.let {
            return if (BCrypt.checkpw(user.password, it[UserTable.password])) {
                it[UserTable.id]
            } else {
                null
            }
        } ?: return null
    }

    override suspend fun checkUserId(userId: UUID): Boolean {
        return transaction {
            UserTable
                .select(UserTable.id)
                .where { UserTable.id eq userId }
                .count() > 0
        }
    }
}