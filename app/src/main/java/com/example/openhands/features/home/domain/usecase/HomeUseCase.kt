package com.example.openhands.features.home.domain.usecase

import com.example.openhands.features.home.data.model.TranslationHistoryItem
import com.example.openhands.features.home.domain.repository.IHomeRepository

class HomeUseCase(
    private val repository: IHomeRepository
) {
    // Función para obtener el historial, ordenado del más nuevo al más viejo.
    suspend fun getHistory(): List<TranslationHistoryItem> {
        return repository.getTranslationHistory().sortedByDescending { it.timestamp }
    }

    // Función para guardar una traducción.
    suspend fun saveTranslation(text: String) {
        repository.saveTranslation(text)
    }
}