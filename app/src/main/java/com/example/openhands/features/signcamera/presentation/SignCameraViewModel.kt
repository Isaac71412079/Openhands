package com.example.openhands.features.signcamera.presentation

import android.graphics.Bitmap
import android.util.Log // Añadir Log para depuración
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openhands.features.signcamera.domain.repository.ISignCameraRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SignCameraViewModel(
    private val repository: ISignCameraRepository
) : ViewModel() {

    // Estado visible en la UI
    var detectedText by mutableStateOf("...")
        private set

    // Job para recolectar el texto
    private var textCollectorJob: Job? = null

    // Configura tu URL de ngrok aquí
    private val SERVER_URL = "https://2c63074bfac7.ngrok-free.app"

    init {
        // Iniciar la conexión SocketIO
        repository.connectSocket(SERVER_URL)

        // Iniciar la recolección del texto
        // --- CORRECCIÓN: CAMBIAR A Dispatchers.Main ---
        textCollectorJob = viewModelScope.launch(Dispatchers.Main) {
            repository.detectedText.collectLatest { newText ->
                Log.d("SignCameraVM", "UI UPDATE: Texto recibido y guardado en State: $newText")

                detectedText = newText
                Log.d("SignCameraVM", "UI UPDATE: Texto recibido -> $newText") // Log para verificar la UI
            }
        }
    }

    fun sendFrame(bitmap: Bitmap) {
        // Ejecutar en un hilo de IO para no bloquear la UI al hacer la compresión
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendCameraFrame(bitmap)
        }
    }

    fun clearText() {
        repository.clearText()
    }

    fun clearResources() {
        textCollectorJob?.cancel()
        repository.disconnectSocket()
    }
}