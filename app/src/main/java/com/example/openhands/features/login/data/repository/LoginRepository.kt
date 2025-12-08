package com.example.openhands.features.login.data.repository

import com.example.openhands.features.login.data.LoginDataStore
import com.example.openhands.features.login.domain.model.LoginResult
import com.example.openhands.features.login.domain.repository.ILoginRepository
import com.google.firebase.FirebaseNetworkException // Importante importar esto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.tasks.await

class LoginRepository(
    private val loginDataStore: LoginDataStore,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : ILoginRepository {

    override suspend fun login(email: String, pass: String): LoginResult {
        return try {
            // Intentamos iniciar sesión en Firebase
            firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            saveSession(email)
            LoginResult.Success
        } catch (e: Exception) {
            e.printStackTrace()

            // AQUÍ ESTÁ LA MAGIA DEL PASO 5:
            // Diferenciamos qué tipo de error ocurrió
            when (e) {
                is FirebaseNetworkException -> {
                    // Esto ocurre cuando no hay internet o no se alcanza el servidor
                    LoginResult.Failure.Unknown("No hay conexión a internet. Verifique su red.")
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    // Contraseña o correo malformado/erróneo
                    LoginResult.Failure.InvalidCredentials
                }
                else -> {
                    // Cualquier otro error
                    LoginResult.Failure.Unknown(e.message ?: "Ocurrió un error inesperado.")
                }
            }
        }
    }

    override suspend fun saveSession(email: String) {
        loginDataStore.saveUserEmail(email)
    }
}