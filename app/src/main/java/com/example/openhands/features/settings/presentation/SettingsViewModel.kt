package com.example.openhands.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.settings.data.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsDataStore: SettingsDataStore) : ViewModel() {

    val themePreference = settingsDataStore.themePreference.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsDataStore.THEME_SYSTEM
    )

    fun setThemePreference(themeValue: Int) {
        viewModelScope.launch {
            settingsDataStore.setThemePreference(themeValue)
        }
    }
}