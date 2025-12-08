package com.example.openhands.features.auth.presentation

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1. Estado de UI
data class RegisterUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val genericError: String? = null
)

// 2. CAMBIO: Recibimos auth y db por constructor
class RegisterViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun registerUser(
        email: String,
        password: String,
        confirmPassword: String
    ) {
        // Validar localmente ANTES de llamar a Firebase
        if (!validateInputs(email, password, confirmPassword)) {
            return
        }

        _uiState.value = RegisterUiState(isLoading = true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                val userMap = mapOf(
                    "uid" to uid,
                    "email" to email,
                    "creadoEn" to com.google.firebase.Timestamp.now()
                )

                db.collection("users").document(uid).set(userMap)
                    .addOnSuccessListener { _uiState.value = RegisterUiState(success = true) }
                    .addOnFailureListener { _uiState.value = RegisterUiState(genericError = "Error al guardar los datos.") }
            }
            .addOnFailureListener { exception ->
                val errorMessage = when (exception) {
                    is FirebaseAuthInvalidCredentialsException -> "El formato del correo electrónico es incorrecto."
                    is FirebaseAuthUserCollisionException -> "El correo electrónico ya está en uso."
                    else -> exception.message ?: "Ocurrió un error desconocido."
                }
                _uiState.value = RegisterUiState(emailError = errorMessage)
            }
    }

    private fun validateInputs(email: String, password: String, confirmPassword: String): Boolean {
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        // CAMBIO: Usamos Regex de Kotlin en lugar de Patterns de Android para evitar errores en Tests
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

        if (!emailRegex.matches(email)) {
            emailError = "Correo electrónico no válido."
        }

        if (password.length < 6) {
            passwordError = "La contraseña debe tener al menos 6 caracteres."
        }

        if (password != confirmPassword) {
            passwordError = "Las contraseñas no coinciden."
            confirmPasswordError = "Las contraseñas no coinciden."
        }

        val hasError = emailError != null || passwordError != null || confirmPasswordError != null
        if (hasError) {
            _uiState.value = RegisterUiState(
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }
        return !hasError
    }
}