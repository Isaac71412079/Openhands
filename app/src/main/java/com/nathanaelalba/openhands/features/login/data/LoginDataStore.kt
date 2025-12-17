package com.nathanaelalba.openhands.features.login.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class LoginDataStore(
    private val context: Context
) {
    companion object {
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

    fun getToken(): kotlinx.coroutines.flow.Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_TOKEN]
        }
    }

    fun getUserEmail(): kotlinx.coroutines.flow.Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_EMAIL]
        }
    }

    suspend fun clearData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}