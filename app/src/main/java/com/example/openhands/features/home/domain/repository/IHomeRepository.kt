package com.example.openhands.features.home.domain.repository

import com.example.openhands.features.home.data.model.TranslationHistoryItem

interface IHomeRepository {

    suspend fun saveTranslation(text: String)
    suspend fun getTranslationHistory(): List<TranslationHistoryItem>
}