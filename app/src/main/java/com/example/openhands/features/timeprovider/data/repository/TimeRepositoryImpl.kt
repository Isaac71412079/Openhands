package com.example.openhands.features.timeprovider.data.repository

import com.example.openhands.features.timeprovider.data.datasource.TimeLocalDataSource
import com.example.openhands.features.timeprovider.data.datasource.TimeRemoteDataSource
import com.example.openhands.features.timeprovider.domain.repository.TimeRepository

class TimeRepositoryImpl(
    private val remote: TimeRemoteDataSource,
    private val local: TimeLocalDataSource
) : TimeRepository {

    override suspend fun getRealTime(): String {
        return try {
            val time = remote.getServerTime()
            local.saveTime(time)
            time
        } catch (e: Exception) {
            local.getLastTime() ?: "No disponible"
        }
    }
}
