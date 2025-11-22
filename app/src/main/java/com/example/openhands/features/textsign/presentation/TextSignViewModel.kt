package com.example.openhands.features.textsign.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.home.domain.usecase.HomeUseCase
import com.example.openhands.features.textsign.domain.usecase.TranslateTextUseCase
import kotlinx.coroutines.launch
import com.example.openhands.R

class TextSignViewModel(
    private val translateTextUseCase: TranslateTextUseCase,
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    var inputText by mutableStateOf("")
        private set
/*
    var imageResId by mutableStateOf<Int?>(null)
        private set
*/
    var mediaResId by mutableStateOf<Int?>(null)
        private set

    fun onTextChanged(newText: String) {
        inputText = newText
        if (newText.isEmpty()) {
            mediaResId = null
        }
    }

    fun onTranslateClicked() {
        val textToTranslate = inputText.trim()
        if (textToTranslate.isBlank()) return
        mediaResId = when (textToTranslate.lowercase()) {
            //"a" -> R.drawable.a
            "a" -> R.raw.letra_a
            "b" -> R.drawable.b
            "c" -> R.drawable.c
            "d" -> R.drawable.d
            "e" -> R.drawable.e
            "f" -> R.drawable.f
            "g" -> R.drawable.g
            "h" -> R.drawable.h
            "i" -> R.drawable.i
            "j" -> R.drawable.j
            "k" -> R.drawable.k
            "l" -> R.drawable.l
            "ll" -> R.drawable.ll
            "m" -> R.drawable.m
            "n" -> R.drawable.n
            "ñ" -> R.drawable.ne
            "o" -> R.drawable.o
            "p" -> R.drawable.p
            "q" -> R.drawable.q
            "r" -> R.drawable.r
            "s" -> R.drawable.s
            "t" -> R.drawable.t
            "u" -> R.drawable.u
            "v" -> R.drawable.v
            "w" -> R.drawable.w
            "x" -> R.drawable.x
            "y" -> R.drawable.y
            "z" -> R.drawable.z
            else -> null // Si no es una letra del alfabeto, no mostramos imagen
        }
        viewModelScope.launch {
            homeUseCase.saveTranslation(textToTranslate)
            translateTextUseCase.invoke(textToTranslate)
        }
    }
}