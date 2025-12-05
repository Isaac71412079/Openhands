package com.example.openhands.features.timeprovider.domain.usecase

import com.example.openhands.features.timeprovider.domain.repository.TimeRepository

class GetRealTimeUseCase(private val repository: TimeRepository) {

    suspend operator fun invoke(): String {
        return repository.getRealTime()
    }
}