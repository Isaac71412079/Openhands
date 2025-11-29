package com.example.openhands.features.clock.data.remote

import kotlinx.coroutines.delay

class TimeDataSource {
    // Simula una llamada a API que devuelve el Timestamp actual en milisegundos (UTC)
    suspend fun fetchServerTime(): Long {
        delay(500) // Simula lag de red
        return System.currentTimeMillis() // En producción, esto viene del JSON del Backend
    }
}