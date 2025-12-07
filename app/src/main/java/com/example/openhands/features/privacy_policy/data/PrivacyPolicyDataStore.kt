package com.example.openhands.features.privacy_policy.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "privacy_policy_preferences")

class PrivacyPolicyDataStore(context: Context) {

    private val appContext = context.applicationContext

    companion object {
        val PRIVACY_POLICY_ACCEPTED = booleanPreferencesKey("privacy_policy_accepted")
    }

    val hasAcceptedPrivacyPolicy: Flow<Boolean> = appContext.dataStore.data.map {
        it[PRIVACY_POLICY_ACCEPTED] ?: false
    }

    suspend fun setPrivacyPolicyAccepted(accepted: Boolean) {
        appContext.dataStore.edit {
            it[PRIVACY_POLICY_ACCEPTED] = accepted
        }
    }
}
