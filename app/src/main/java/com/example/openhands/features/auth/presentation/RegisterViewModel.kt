package com.example.openhands.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class RegisterUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun registerUser(
        email: String,
        password: String,
        confirmPassword: String
    ) {
        // 🔹 Validaciones básicas
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _uiState.value = RegisterUiState(errorMessage = "Completa todos los campos")
            return
        }

        if (password != confirmPassword) {
            _uiState.value = RegisterUiState(errorMessage = "Las contraseñas no coinciden")
            return
        }

        // 🔹 Loading
        _uiState.value = RegisterUiState(isLoading = true)

        // 🔥 Crear usuario con Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->

                val uid = result.user?.uid ?: return@addOnSuccessListener

                // 🔥 Datos mínimos almacenados en Firestore
                val userMap = mapOf(
                    "uid" to uid,
                    "email" to email,
                    "creadoEn" to com.google.firebase.Timestamp.now()
                )

                db.collection("users")
                    .document(uid)
                    .set(userMap)
                    .addOnSuccessListener {
                        _uiState.value = RegisterUiState(success = true)
                    }
                    .addOnFailureListener {
                        _uiState.value = RegisterUiState(errorMessage = "Error guardando datos en Firestore")
                    }
            }
            .addOnFailureListener {
                _uiState.value = RegisterUiState(errorMessage = it.message ?: "Error desconocido")
            }
    }
}