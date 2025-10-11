package com.example.openhands.features.textsign.domain.usecase

import com.example.openhands.features.textsign.domain.repository.ITextSignRepository

class TranslateTextUseCase(
    private val repository: ITextSignRepository
) {
    suspend fun invoke(text: String) {
        if (text.isBlank()) {
            return
        }
    }
}