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

    var videoQueue by mutableStateOf<List<Int>>(emptyList()) // <--- Añade esta línea
        private set

    fun onTextChanged(newText: String) {
        inputText = newText
        if (newText.isEmpty()) {
            videoQueue = emptyList() // Limpia la cola de videos
        }
    }

    fun onTranslateClicked() {
        val textToTranslate = inputText.trim().lowercase()
        if (textToTranslate.isBlank()) {
            videoQueue = emptyList()
            return
        }

        val newVideoQueue = mutableListOf<Int>()

        val exceptions = mapOf(
            "hola" to R.raw.hola,
            "mundo" to R.raw.mundo

        )

        val words = textToTranslate.split(' ').filter { it.isNotBlank() }

        for (word in words) {
            if (exceptions.containsKey(word)) {
                // La palabra es una excepción, añadimos solo su video específico
                newVideoQueue.add(exceptions[word]!!)
            } else {
                // La palabra no es una excepción, la deletreamos
                for (char in word) {
                    val videoResId = when (char.toString()) {
                        "a" -> R.raw.letra_a
                        "b" -> R.raw.letra_b
                        "c" -> R.raw.letra_c
                        "d" -> R.raw.letra_d
                        "e" -> R.raw.letra_e
                        "f" -> R.raw.letra_f
                        "g" -> R.raw.letra_g
                        "h" -> R.raw.letra_h
                        "i" -> R.raw.letra_i
                        "j" -> R.raw.letra_j
                        "k" -> R.raw.letra_k
                        "l" -> R.raw.letra_l
                        "ll" -> R.raw.letra_ll
                        "m" -> R.raw.letra_m
                        "n" -> R.raw.letra_n
                        "ñ" -> R.raw.letra_ne
                        "o" -> R.raw.letra_o
                        "p" -> R.raw.letra_p
                        "q" -> R.raw.letra_q
                        "r" -> R.raw.letra_r
                        "rr" -> R.raw.letra_rr
                        "s" -> R.raw.letra_s
                        "t" -> R.raw.letra_t
                        "u" -> R.raw.letra_u
                        "v" -> R.raw.letra_v
                        "w" -> R.raw.letra_w
                        "x" -> R.raw.letra_x
                        "y" -> R.raw.letra_y
                        "z" -> R.raw.letra_z
                        else -> null
                    }
                    videoResId?.let { newVideoQueue.add(it) }
                }
            }
        }

        videoQueue = newVideoQueue
        viewModelScope.launch {
            homeUseCase.saveTranslation(textToTranslate)
            translateTextUseCase.invoke(textToTranslate)
        }

    }
}