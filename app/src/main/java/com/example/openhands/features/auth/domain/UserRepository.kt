package com.example.openhands.features.auth.domain

import com.example.openhands.features.auth.data.User
import com.example.openhands.features.auth.data.UserDao

class UserRepository(private val dao: UserDao) {
    suspend fun registerUser(user: User) = dao.insertUser(user)
}
