package com.example.openhands.features.textsign.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.home.domain.usecase.HomeUseCase // Importante
import com.example.openhands.features.textsign.domain.usecase.TranslateTextUseCase
import kotlinx.coroutines.launch
import com.example.openhands.R

class TextSignViewModel(
    private val translateTextUseCase: TranslateTextUseCase,
    private val homeUseCase: HomeUseCase // Añadimos la inyección
) : ViewModel() {

    var inputText by mutableStateOf("")
        private set

    var imageResId by mutableStateOf<Int?>(null)
        private set

    fun onTextChanged(newText: String) {
        inputText = newText
        if (newText.isEmpty()) {
            imageResId = null
        }
    }

    fun onTranslateClicked() {
        val textToTranslate = inputText.trim()
        if (textToTranslate.isBlank()) return
        imageResId = when (textToTranslate.lowercase()) {
            "a" -> R.drawable.a
            "b" -> R.drawable.b
            "c" -> R.drawable.c
            else -> null // Si no es una letra del alfabeto, no mostramos imagen
        }

        viewModelScope.launch {
            homeUseCase.saveTranslation(textToTranslate)
            translateTextUseCase.invoke(textToTranslate)
        }
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