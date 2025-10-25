package com.example.openhands.features.home.domain.usecase

import com.example.openhands.features.home.data.model.TranslationHistoryItem
import com.example.openhands.features.home.domain.repository.IHomeRepository

class HomeUseCase(
    private val repository: IHomeRepository
) {
    suspend fun getHistory(): List<TranslationHistoryItem> {
        return repository.getTranslationHistory().sortedByDescending { it.timestamp }
    }

    suspend fun saveTranslation(text: String) {
        repository.saveTranslation(text)
    }
}