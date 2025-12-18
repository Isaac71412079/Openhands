package com.nathanaelalba.openhands.features.appupdate.domain

import com.nathanaelalba.openhands.features.appupdate.data.AppUpdateRepository

class AppUpdateUseCase(private val repository: AppUpdateRepository) {

    suspend fun shouldShowUpdate(currentVersion: Int): AppUpdateConfig? {

        val config = repository.getAppUpdateConfig() ?: return null
        val lastSeen = repository.getLastSeenVersion()

        return if (
            config.latestVersionCode > currentVersion &&
            config.latestVersionCode > lastSeen
        ) {
            config
        } else null
    }

    suspend fun markAsSeen(version: Long) {
        repository.saveLastSeenVersion(version)
    }
}