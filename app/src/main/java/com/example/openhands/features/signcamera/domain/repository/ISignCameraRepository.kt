package com.example.openhands.features.signcamera.domain.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.SharedFlow

interface ISignCameraRepository {
    val detectedText: SharedFlow<String> // Para que el ViewModel escuche el texto

    fun connectSocket(serverUrl: String)
    fun disconnectSocket()
    fun sendCameraFrame(bitmap: Bitmap)
    fun clearText()
}