package com.example.openhands.features.login.domain.usecase

import com.example.openhands.features.login.domain.model.LoginResult
import com.example.openhands.features.login.domain.repository.ILoginRepository

class LoginUseCase(
    private val repository: ILoginRepository
) {
    suspend fun invoke(email: String, pass: String): LoginResult {
        val loginResult = repository.login(email, pass)
        /*if (email.isBlank() || pass.isBlank()) {
            return LoginResult.Failure.EmptyFields
        }*/
        if (loginResult is LoginResult.Success) {
            // Llama a la nueva función del repositorio para guardar la sesión
            repository.saveSession(email)
        }
        //return repository.login(email, pass)
        return loginResult
    }
}