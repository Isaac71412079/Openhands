package com.example.openhands.features.clock.domain.usecase

import com.example.openhands.features.clock.domain.repository.ClockRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRealTimeUseCase(private val repository: ClockRepository) {

    // Retorna un flujo que emite la hora real cada segundo
    operator fun invoke(): Flow<Long> = flow {
        // Primero aseguramos la sincronización con el backend
        repository.syncTime()

        // Ciclo infinito que emite la hora cada 1 segundo
        while (true) {
            emit(repository.getCurrentRealTime())
            delay(1000) // Espera 1 segundo
        }
    }
}