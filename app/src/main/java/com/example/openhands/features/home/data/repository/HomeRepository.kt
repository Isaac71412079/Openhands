package com.example.openhands.features.home.data.repository

import com.example.openhands.features.home.data.dao.HistoryDao
import com.example.openhands.features.home.data.model.TranslationHistoryItem
import com.example.openhands.features.home.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow

class HomeRepository(
    private val historyDao: HistoryDao
) : IHomeRepository {

    override suspend fun saveTranslation(text: String) {
        val historyItem = TranslationHistoryItem(
            originalText = text,
            timestamp = System.currentTimeMillis()
        )
        historyDao.insert(historyItem)
    }

    override fun getTranslationHistory(): Flow<List<TranslationHistoryItem>> {
        return historyDao.getHistory()
    }

    // 3. Implementación de la función para borrar el historial.
    override suspend fun clearHistory() {
        historyDao.clearAll()
    }
}
