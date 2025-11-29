package com.example.openhands.features.clock.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.clock.domain.usecase.GetRealTimeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClockViewModel(
    private val getRealTimeUseCase: GetRealTimeUseCase
) : ViewModel() {

    private val _timeState = MutableStateFlow("Sincronizando...")
    val timeState: StateFlow<String> = _timeState

    init {
        startClock()
    }

    private fun startClock() {
        viewModelScope.launch {
            getRealTimeUseCase().collect { realTimeMillis ->
                // Formatear milisegundos a hora legible (HH:mm:ss)
                val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                _timeState.value = formatter.format(Date(realTimeMillis))
            }
        }
    }
}