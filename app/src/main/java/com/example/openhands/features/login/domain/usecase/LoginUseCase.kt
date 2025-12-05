package com.example.openhands.features.login.domain.usecase

import com.example.openhands.features.login.domain.model.LoginResult
import com.example.openhands.features.login.domain.repository.ILoginRepository

class LoginUseCase(private val repository: ILoginRepository) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        if (email.isBlank() || password.isBlank()) {
            return LoginResult.Failure.EmptyFields
        }
        return repository.login(email, password)

    }
}