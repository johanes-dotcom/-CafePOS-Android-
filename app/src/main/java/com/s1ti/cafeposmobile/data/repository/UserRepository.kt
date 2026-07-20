package com.s1ti.cafeposmobile.data.repository

import com.s1ti.cafeposmobile.data.local.dao.UserDao
import com.s1ti.cafeposmobile.data.local.entity.UserEntity
import com.s1ti.cafeposmobile.util.PasswordHasher
import kotlinx.coroutines.flow.Flow

sealed class LoginResult {
    data class Success(val user: UserEntity) : LoginResult()
    object InvalidCredentials : LoginResult()
    object EmptyInput : LoginResult()
}

class UserRepository(private val userDao: UserDao) {

    /**
     * REQ-1.2: validasi username & password sebelum akses diberikan.
     */
    suspend fun login(username: String, password: String): LoginResult {
        if (username.isBlank() || password.isBlank()) {
            return LoginResult.EmptyInput
        }
        val user = userDao.getActiveUserByUsername(username.trim())
            ?: return LoginResult.InvalidCredentials

        return if (PasswordHasher.verify(password, user.passwordHash)) {
            LoginResult.Success(user)
        } else {
            LoginResult.InvalidCredentials
        }
    }

    suspend fun getUserByUsername(username: String): UserEntity? {
        return userDao.getActiveUserByUsername(username)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    fun getAllUsers(): Flow<List<UserEntity>> {
        return userDao.getAllUsers()
    }
}
