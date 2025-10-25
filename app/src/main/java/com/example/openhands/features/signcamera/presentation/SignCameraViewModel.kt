package com.example.openhands.features.signcamera.presentation

import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.concurrent.Executor

class SignCameraViewModel : ViewModel() {

    // Estado para el texto detectado (lo mantenemos como antes)
    var detectedText by mutableStateOf("Traducción aparecerá aquí...")
        private set

    // --- NUEVO ESTADO ---
    // Guardará la foto capturada como un Bitmap. Si es 'null', mostramos la cámara en vivo.
    var capturedImageBitmap by mutableStateOf<Bitmap?>(null)
        private set

    /**
     * La función principal que se llamará desde la UI para tomar una foto.
     */
    fun onTakePhotoClicked(cameraController: LifecycleCameraController, executor: Executor) {
        cameraController.takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    // Cuando la foto se toma con éxito, la convertimos a Bitmap
                    // y actualizamos nuestro estado.
                    capturedImageBitmap = image.toBitmap() // Usaremos una función de ayuda
                    image.close() // ¡Muy importante cerrar el ImageProxy!
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    println("Error al tomar la foto: ${exception.message}")
                }
            }
        )
    }

    /**
     * Función para descartar la foto actual y volver a la vista previa de la cámara.
     */
    fun onRetakePhotoClicked() {
        capturedImageBitmap = null
    }

    /**
     * Función para confirmar la foto y enviarla a analizar (futura implementación).
     */
    fun onConfirmPhotoClicked() {
        // TODO: Aquí es donde enviarías el 'capturedImageBitmap' al
        // UseCase para que el modelo de ML lo analice.

        // Por ahora, simulamos una detección y volvemos a la cámara.
        detectedText = "Foto analizada (simulado)"
        onRetakePhotoClicked()
    }
}