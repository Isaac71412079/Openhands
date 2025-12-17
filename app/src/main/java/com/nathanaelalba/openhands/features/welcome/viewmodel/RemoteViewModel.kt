package com.nathanaelalba.openhands.features.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RemoteViewModel : ViewModel() {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    private val _welcomeMessage = MutableStateFlow(
        "Traductor bidireccional de Lengua de Señas Boliviana (LSB). Aprende, comunica y conecta con facilidad"
    )
    val welcomeMessage: StateFlow<String> = _welcomeMessage

    init {
        setupRemoteConfig()
        fetchWelcomeMessage()
    }

    private fun setupRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600 // 1 hora
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Valores por defecto (fallback)
        remoteConfig.setDefaultsAsync(
            mapOf(
                "welcome_message" to _welcomeMessage.value
            )
        )
    }

    private fun fetchWelcomeMessage() {
        viewModelScope.launch {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _welcomeMessage.value =
                            remoteConfig.getString("welcome_message")
                    }
                }
        }
    }
}