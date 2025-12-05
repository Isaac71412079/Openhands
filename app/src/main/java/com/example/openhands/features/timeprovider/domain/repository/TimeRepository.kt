package com.example.openhands.features.timeprovider.domain.repository

interface TimeRepository {
    suspend fun getRealTime(): String
}