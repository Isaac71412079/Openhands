package com.example.openhands.features.login.presentation

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.login.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

// 1. Estado de UI más detallado
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
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onLoginClicked() {
        if (!validateInputs()) {
            return
        }

        uiState = LoginUIState(isLoading = true)

        viewModelScope.launch {
            try {
                loginUseCase(email, password)
                uiState = LoginUIState(success = true)
            } catch (e: Exception) {
                val errorState = when (e) {
                    is FirebaseAuthInvalidUserException -> LoginUIState(emailError = "El correo no está registrado.")
                    is FirebaseAuthInvalidCredentialsException -> LoginUIState(passwordError = "Contraseña incorrecta.")
                    else -> LoginUIState(genericError = e.message ?: "Ocurrió un error inesperado.")
                }
                uiState = errorState
            }
        }
    }

    private fun validateInputs(): Boolean {
        var emailError: String? = null
        var passwordError: String? = null

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Correo electrónico no válido."
        }

        if (password.isBlank()) {
            passwordError = "La contraseña no puede estar vacía."
        }

        val hasError = emailError != null || passwordError != null
        if (hasError) {
            uiState = LoginUIState(emailError = emailError, passwordError = passwordError)
        }
        return !hasError
    }
}