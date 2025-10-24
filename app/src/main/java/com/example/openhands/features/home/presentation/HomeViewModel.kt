package com.example.openhands.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.home.data.model.TranslationHistoryItem
import com.example.openhands.features.home.domain.usecase.HomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    private val _historyState = MutableStateFlow<List<TranslationHistoryItem>>(emptyList())
    val historyState = _historyState.asStateFlow()

    init {
        loadHistory()
    }

    // Se puede llamar a esta función para recargar el historial si es necesario
    fun loadHistory() {
        viewModelScope.launch {
            _historyState.value = homeUseCase.getHistory()
        }
    }
}