package com.example.openhands.features.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class SettingsDataStore(context: Context) {

    private val appContext = context.applicationContext

    companion object {
        // 0 = Sistema, 2 = Oscuro (se elimina el 1 = Claro)
        val THEME_PREFERENCE_KEY = intPreferencesKey("theme_preference")
        const val THEME_SYSTEM = 0
        const val THEME_DARK = 2
    }

    val themePreference: Flow<Int> = appContext.settingsDataStore.data.map { preferences ->
        preferences[THEME_PREFERENCE_KEY] ?: THEME_SYSTEM
    }

    suspend fun setThemePreference(themeValue: Int) {
        appContext.settingsDataStore.edit { preferences ->
            preferences[THEME_PREFERENCE_KEY] = themeValue
        }
    }
}