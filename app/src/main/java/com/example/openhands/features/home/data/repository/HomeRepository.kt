package com.example.openhands.features.home.data.repository

import com.example.openhands.features.home.data.model.TranslationHistoryItem
import com.example.openhands.features.home.domain.repository.IHomeRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class HomeRepository : IHomeRepository {

    private val database = FirebaseDatabase.getInstance()
    private val userId = "fixed_user_for_testing" // Usaremos un ID fijo para la prueba
    private val historyRef = database.getReference("history").child(userId)

    /**
     * Guarda una nueva traducción en el historial del usuario en Firebase.
     */
    override suspend fun saveTranslation(text: String) {
        // Generamos un ID único para esta nueva entrada de historial.
        val newEntryId = historyRef.push().key ?: return

        // Creamos el objeto que vamos a guardar.
        val historyItem = TranslationHistoryItem(
            id = newEntryId,
            originalText = text,
            timestamp = System.currentTimeMillis() // Guardamos la fecha y hora actual.
        )

        // Guardamos el objeto en la base de datos usando el ID único como clave.
        historyRef.child(newEntryId).setValue(historyItem).await()
    }

    /**
     * Obtiene la lista completa del historial de traducciones desde Firebase.
     */
    override suspend fun getTranslationHistory(): List<TranslationHistoryItem> {
        return try {
            // Hacemos una petición para obtener todos los datos del nodo del usuario.
            val dataSnapshot = historyRef.get().await()

            // Convertimos la respuesta de Firebase a una lista de nuestros objetos.
            val historyList = dataSnapshot.children.mapNotNull { snapshot ->
                snapshot.getValue(TranslationHistoryItem::class.java)
            }

            historyList

        } catch (e: Exception) {
            // Si hay un error (ej. sin internet), devolvemos una lista vacía.
            emptyList()
        }
    }
}