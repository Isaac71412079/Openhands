package com.example.openhands.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun registerUser(nombre: String, email: String, password: String, confirmPassword: String) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _uiState.value = RegisterUiState(errorMessage = "Completa todos los campos")
            return
        }

        if (password != confirmPassword) {
            _uiState.value = RegisterUiState(errorMessage = "Las contraseñas no coinciden")
            return
        }

        // Simulamos la inserción en base de datos
        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)
            kotlinx.coroutines.delay(1500) // Simular red o BD

            // Aquí podrías llamar al repositorio que inserta en Room o Firebase
            // Ejemplo: repository.registerUser(User(nombre = nombre, email = email, password = password))

            _uiState.value = RegisterUiState(success = true)
        }
    }
}
