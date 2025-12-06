package com.example.openhands.features.login.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.login.domain.model.LoginResult
import com.example.openhands.features.login.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch

// 1. Estado de UI más detallado, similar a Register
data class LoginUIState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val genericError: String? = null
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var uiState by mutableStateOf(LoginUIState())
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
        // Limpia el error al empezar a escribir de nuevo
        if (uiState.emailError != null || uiState.genericError != null) {
            uiState = uiState.copy(emailError = null, genericError = null)
        }
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        // Limpia el error al empezar a escribir de nuevo
        if (uiState.passwordError != null || uiState.genericError != null) {
            uiState = uiState.copy(passwordError = null, genericError = null)
        }
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            uiState = LoginUIState(isLoading = true)
            // 2. Evaluar el resultado del UseCase para actuar en consecuencia
            when (val result = loginUseCase(email, password)) {
                is LoginResult.Success -> {
                    uiState = LoginUIState(success = true)
                }
                is LoginResult.Failure.EmptyFields -> {
                    uiState = LoginUIState(
                        emailError = if(email.isBlank()) "El correo no puede estar vacío" else null,
                        passwordError = if(password.isBlank()) "La contraseña no puede estar vacía" else null
                    )
                }
                is LoginResult.Failure.InvalidCredentials -> {
                    uiState = LoginUIState(genericError = "Correo o contraseña incorrectos.")
                }
                // 4. Corregido: Ahora `result` siempre tiene la propiedad `message`.
                is LoginResult.Failure -> {
                    uiState = LoginUIState(genericError = result.message ?: "Ocurrió un error inesperado.")
                }
            }
        }
    }
}