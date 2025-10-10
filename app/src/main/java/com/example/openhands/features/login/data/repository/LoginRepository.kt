package com.example.openhands.features.login.data.repository

import com.example.openhands.features.login.data.LoginDataStore
import com.example.openhands.features.login.domain.model.LoginResult
import com.example.openhands.features.login.domain.repository.ILoginRepository
import kotlinx.coroutines.delay

class LoginRepository(
    private val loginDataStore: LoginDataStore
) : ILoginRepository {
    override suspend fun login(email: String, pass: String): LoginResult {
        delay(1000)
        return if (email == "openhands@gmail.com" && pass == "123") {
            LoginResult.Success
        } else {
            LoginResult.Failure.InvalidCredentials
        }
    }

    override suspend fun saveSession(email: String) {
        loginDataStore.saveUserEmail(email)
    }
}