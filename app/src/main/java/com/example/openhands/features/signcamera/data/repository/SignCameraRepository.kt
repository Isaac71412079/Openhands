package com.example.openhands.features.signcamera.data.repository

import android.graphics.Bitmap
import com.example.openhands.features.signcamera.data.SocketManager
import com.example.openhands.features.signcamera.domain.repository.ISignCameraRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SignCameraRepository : ISignCameraRepository {

    private lateinit var socketManager: SocketManager

    // Flujo para emitir el texto recibido al ViewModel
    private val _detectedText = MutableSharedFlow<String>(replay = 1)
    override val detectedText: SharedFlow<String> = _detectedText.asSharedFlow()

    override fun connectSocket(serverUrl: String) {
        socketManager = SocketManager(serverUrl).apply {
            onTextReceived = { text ->
                _detectedText.tryEmit(text) // Emitir el texto recibido del servidor
            }
            connect()
        }
    }

    override fun disconnectSocket() {
        if (this::socketManager.isInitialized) {
            socketManager.disconnect()
        }
    }

    override fun sendCameraFrame(bitmap: Bitmap) {
        if (this::socketManager.isInitialized) {
            socketManager.sendFrame(bitmap)
        }
    }

    override fun clearText() {
        if (this::socketManager.isInitialized) {
            socketManager.sendClearText()
        }
    }
}