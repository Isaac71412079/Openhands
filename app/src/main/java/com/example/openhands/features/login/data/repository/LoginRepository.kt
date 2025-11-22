package com.example.openhands.features.login.data.repository

import com.example.openhands.features.login.data.LoginDataStore
import com.example.openhands.features.login.domain.model.LoginResult
import com.example.openhands.features.login.domain.repository.ILoginRepository
import com.google.firebase.auth.FirebaseAuth
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
            LoginResult.Failure.InvalidCredentials
        }
    }

    override suspend fun saveSession(email: String) {
        loginDataStore.saveUserEmail(email)
    }
}
