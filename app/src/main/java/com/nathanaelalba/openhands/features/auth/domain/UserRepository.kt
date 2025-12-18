package com.nathanaelalba.openhands.features.auth.domain

import com.nathanaelalba.openhands.features.auth.data.User
import com.nathanaelalba.openhands.features.auth.data.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val dao: UserDao
) {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /**
     * Registra al usuario en:
     * 1) Firebase Authentication
     * 2) Firestore
     * 3) (opcional) Room
     */
    suspend fun registerUser(user: User, password: String): Result<Unit> {
        return try {
            // 1️⃣ Crear usuario en Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(user.email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("No se pudo obtener UID")

            // 2️⃣ Guardar datos en Firestore
            val userMap = mapOf(
                "uid" to uid,
                "nombre" to user.nombre,
                "apellido" to user.apellido,
                "email" to user.email
            )

            db.collection("users")
                .document(uid)
                .set(userMap)
                .await()

            // 3️⃣ (Opcional) Guardar en Room local
            dao.insertUser(user)

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
