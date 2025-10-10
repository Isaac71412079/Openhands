package com.example.openhands.features.login.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.login.domain.model.LoginResult
import com.example.openhands.features.login.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch
import com.example.openhands.features.login.data.LoginDataStore
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loginDataStore: LoginDataStore
) : ViewModel() {

    // Estado para los campos de texto
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    // Estado general de la UI
    var uiState by mutableStateOf<LoginUIState>(LoginUIState.Idle)
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            uiState = LoginUIState.Loading
            val result = loginUseCase.invoke(email, password)
            uiState = when (result) {
                is LoginResult.Success -> LoginUIState.Success
                is LoginResult.Failure.EmptyFields -> LoginUIState.Error("Correo y contraseña no pueden estar vacíos.")
                is LoginResult.Failure.InvalidCredentials -> LoginUIState.Error("Credenciales inválidas.")
            }
        }
    }
}

sealed class LoginUIState {
    object Idle : LoginUIState()
    object Loading : LoginUIState()
    object Success : LoginUIState()
    data class Error(val message: String) : LoginUIState()
}