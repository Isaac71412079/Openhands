package com.example.openhands.features.textsign.domain.usecase

import com.example.openhands.features.textsign.domain.repository.ITextSignRepository

class TranslateTextUseCase(
    private val repository: ITextSignRepository
) {
    // Aquí iría la lógica de negocio, como validar que el texto no esté vacío.
    suspend fun invoke(text: String) {
        if (text.isBlank()) {
            // Podríamos retornar un error o no hacer nada
            return
        }
        // repository.translate(text)
    }
}