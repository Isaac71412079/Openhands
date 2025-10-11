package com.example.openhands.features.textsign.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.textsign.domain.usecase.TranslateTextUseCase
import kotlinx.coroutines.launch

class TextSignViewModel(
    private val translateTextUseCase: TranslateTextUseCase
) : ViewModel() {

    var inputText by mutableStateOf("")
        private set

    fun onTextChanged(newText: String) {
        inputText = newText
    }

    fun onTranslateClicked() {
        // Por ahora, no hacemos nada, pero en el futuro llamaríamos al UseCase
        viewModelScope.launch {
            // translateTextUseCase.invoke(inputText)
            // Aquí se manejaría el resultado para mostrarlo en la UI
        }
    }
}