package com.nathanaelalba.openhands.features.appupdate.domain

data class AppUpdateConfig(
    val latestVersionCode: Long = 0,
    val latestVersionName: String = "",
    val title: String = "Actualización disponible",
    val message: String = "",
    val playStoreUrl: String = "",
    val updateOptional: Boolean = true,
    val changelog: List<String> = emptyList() // 👈 CLAVE
)