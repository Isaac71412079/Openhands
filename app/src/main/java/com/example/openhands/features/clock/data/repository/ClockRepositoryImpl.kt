package com.example.openhands.features.clock.data.repository

import android.os.SystemClock
import com.example.openhands.features.clock.data.remote.TimeDataSource
import com.example.openhands.features.clock.domain.repository.ClockRepository

class ClockRepositoryImpl(
    private val dataSource: TimeDataSource
) : ClockRepository {

    private var bootTimeOffset: Long = 0
    private var isSynchronized = false

    override suspend fun syncTime() {
        // 1. Obtenemos la hora real del servidor
        val serverTime = dataSource.fetchServerTime()

        // 2. Obtenemos el tiempo que lleva el celular prendido (esto no cambia si el usuario cambia la fecha)
        val deviceUptime = SystemClock.elapsedRealtime()

        // 3. Calculamos la diferencia: El momento "cero" real
        bootTimeOffset = serverTime - deviceUptime
        isSynchronized = true
    }

    override fun getCurrentRealTime(): Long {
        if (!isSynchronized) return System.currentTimeMillis() // Fallback por si falla

        // La magia: Hora actual = (Hora Servidor - Uptime en ese momento) + Uptime actual
        return bootTimeOffset + SystemClock.elapsedRealtime()
    }
}