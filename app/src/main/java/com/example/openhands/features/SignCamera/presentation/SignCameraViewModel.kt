package com.example.openhands.features.signcamera.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SignCameraViewModel : ViewModel() {
    var detectedText by mutableStateOf("ABCDEFGHIJKLMNOPQ")
        private set
}