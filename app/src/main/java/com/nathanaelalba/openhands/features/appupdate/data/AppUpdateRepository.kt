package com.nathanaelalba.openhands.features.appupdate.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.nathanaelalba.openhands.features.appupdate.domain.AppUpdateConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions

class AppUpdateRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getAppUpdateConfig(): AppUpdateConfig? {
        return try {
            val doc = db.collection("app_config")
                .document("version")
                .get()
                .await()
            doc.toObject(AppUpdateConfig::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getLastSeenVersion(): Long {
        val uid = auth.currentUser?.uid ?: return 0
        val doc = db.collection("users").document(uid).get().await()
        return doc.getLong("lastSeenVersion") ?: 0
    }

    suspend fun saveLastSeenVersion(version: Long) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users")
            .document(uid)
            .set(
                mapOf("lastSeenVersion" to version),
                SetOptions.merge()
            )

    }
}

