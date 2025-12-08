package com.example.openhands.features.auth.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface  UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
}
