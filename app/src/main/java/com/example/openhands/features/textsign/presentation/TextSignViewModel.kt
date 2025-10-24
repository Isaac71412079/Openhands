package com.example.openhands.features.textsign.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.home.domain.usecase.HomeUseCase // Importante
import com.example.openhands.features.textsign.domain.usecase.TranslateTextUseCase
import kotlinx.coroutines.launch

class TextSignViewModel(
    private val translateTextUseCase: TranslateTextUseCase,
    private val homeUseCase: HomeUseCase // Añadimos la inyección
) : ViewModel() {

    var inputText by mutableStateOf("")
        private set

    fun onTextChanged(newText: String) {
        inputText = newText
    }

    fun onTranslateClicked() {
        if (inputText.isNotBlank()) {
            viewModelScope.launch {
                // Guardamos el texto en el historial
                homeUseCase.saveTranslation(inputText)

                // Aquí iría la lógica futura para mostrar la traducción en señas
                translateTextUseCase.invoke(inputText)
            }
        }
    }
}