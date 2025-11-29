package com.example.openhands.features.clock.domain.repository

interface ClockRepository {
    suspend fun syncTime()
    fun getCurrentRealTime(): Long
}