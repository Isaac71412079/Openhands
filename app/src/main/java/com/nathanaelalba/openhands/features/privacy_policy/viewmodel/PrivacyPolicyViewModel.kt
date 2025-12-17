package com.nathanaelalba.openhands.features.privacy_policy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathanaelalba.openhands.features.privacy_policy.data.PrivacyPolicyDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrivacyPolicyViewModel(private val dataStore: PrivacyPolicyDataStore) : ViewModel() {

    val hasAcceptedPrivacyPolicy = dataStore.hasAcceptedPrivacyPolicy.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun acceptPrivacyPolicy() {
        viewModelScope.launch {
            dataStore.setPrivacyPolicyAccepted(true)
        }
    }
}
