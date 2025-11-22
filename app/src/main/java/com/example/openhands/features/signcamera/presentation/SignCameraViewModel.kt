package com.example.openhands.features.signcamera.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignCameraViewModel : ViewModel() {

    // 🔹 Nueva variable para almacenar la URI de la imagen capturada
    var capturedImageUri by mutableStateOf<String?>(null)
        private set


    fun onImageCaptured(uri: String) {
        capturedImageUri = uri
    }
}