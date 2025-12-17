package com.nathanaelalba.openhands.features.home.domain.usecase

import com.nathanaelalba.openhands.features.home.data.model.TranslationHistoryItem
import com.nathanaelalba.openhands.features.home.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow

class HomeUseCase(
    private val repository: IHomeRepository
) {
    fun getHistory(): Flow<List<TranslationHistoryItem>> {
        return repository.getTranslationHistory()
    }

    suspend fun saveTranslation(text: String) {
        repository.saveTranslation(text)
    }

    // 4. Nueva función para exponer la limpieza del historial.
    suspend fun clearHistory() {
        repository.clearHistory()
    }
}
