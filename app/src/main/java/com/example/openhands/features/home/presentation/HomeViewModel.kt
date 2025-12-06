package com.example.openhands.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.home.data.model.TranslationHistoryItem
import com.example.openhands.features.home.domain.usecase.HomeUseCase
import com.example.openhands.features.login.data.LoginDataStore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeUseCase: HomeUseCase,
    private val loginDataStore: LoginDataStore
) : ViewModel() {

    val historyState: StateFlow<List<TranslationHistoryItem>> = homeUseCase.getHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 1. Nuevo StateFlow para exponer el email del usuario.
    val userEmail: StateFlow<String> = loginDataStore.getUserEmail()
        .map { it ?: "" } // Si el email es nulo, devuelve un string vacío
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    fun logout() {
        viewModelScope.launch {
            Firebase.auth.signOut()
            loginDataStore.clearData()
            homeUseCase.clearHistory()
        }
    }
}
