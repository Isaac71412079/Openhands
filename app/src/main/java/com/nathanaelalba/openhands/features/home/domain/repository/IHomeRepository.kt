package com.nathanaelalba.openhands.features.home.domain.repository

import com.nathanaelalba.openhands.features.home.data.model.TranslationHistoryItem
import kotlinx.coroutines.flow.Flow

interface IHomeRepository {
    fun getTranslationHistory(): Flow<List<TranslationHistoryItem>>
    suspend fun saveTranslation(text: String)
    // 2. Nueva función para borrar el historial
    suspend fun clearHistory()
}
