package com.nathanaelalba.openhands.features.signcamera.data

import android.graphics.Bitmap
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class SocketManager(private val serverUrl: String) {

    // Configura la URL de tu túnel ngrok (ej: wss://<ngrok-id>.ngrok.io)
    // ¡RECUERDA CAMBIAR ESTO POR TU URL DE NGROK!
    private val SOCKET_URL = serverUrl

    // Un simple listener para devolver el texto al repositorio/viewmodel
    var onTextReceived: ((String) -> Unit)? = null

    private lateinit var socket: Socket

    fun connect() {
        try {
            val options = IO.Options.builder()
                .setForceNew(true)
                .setTimeout(TimeUnit.SECONDS.toMillis(5))
                .build()

            socket = IO.socket(SOCKET_URL, options)

            socket.on(Socket.EVENT_CONNECT) {
                Log.d("SocketManager", "Conectado al servidor!")
            }
            socket.on(Socket.EVENT_DISCONNECT) {
                Log.d("SocketManager", "Desconectado del servidor.")
            }
            socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e("SocketManager", "Error de conexión: ${args[0]}")
            }

            // 4. El server devuelve la letra/frase completa en el evento 'update_text'
            socket.on("update_text") { args ->
                val data = args[0] as? JSONObject
                val text = data?.getString("text") ?: ""
                Log.i("SocketManager", "SOCKET RECEIVE: $text")

                onTextReceived?.invoke(text) // Notifica al ViewModel
            }

            socket.connect()

        } catch (e: Exception) {
            Log.e("SocketManager", "Error al inicializar SocketIO: ${e.message}")
        }
    }

    fun disconnect() {
        if (this::socket.isInitialized && socket.connected()) {
            socket.disconnect()
        }
    }

    // 3. Envía el frame de la cámara al servidor
    fun sendFrame(bitmap: Bitmap) {
        if (!this::socket.isInitialized || !socket.connected()) {
            return
        }

        // Convertir Bitmap a JPEG Bytes (compresión)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream) // 70% de calidad
        val byteArray = stream.toByteArray()

        // Codificar a Base64 String
        val base64Image = android.util.Base64.encodeToString(byteArray, android.util.Base64.NO_WRAP)

        // Crear el objeto JSON y emitir al evento 'video_frame'
        val data = JSONObject().apply {
            put("image", base64Image)
        }
        socket.emit("video_frame", data)
    }

    fun sendClearText() {
        if (this::socket.isInitialized && socket.connected()) {
            socket.emit("clear_text")
        }
    }
}